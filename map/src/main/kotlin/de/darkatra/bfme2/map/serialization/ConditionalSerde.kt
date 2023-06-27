package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import java.io.OutputStream
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@UseSerdeProperties(ConditionalSerde.Properties::class)
internal class ConditionalSerde(
    serdeFactory: SerdeFactory,
    annotationProcessingContext: AnnotationProcessingContext,
    private val serializationContext: SerializationContext,
    private val preProcessor: PreProcessor<Any>,
    private val postProcessor: PostProcessor<Any>,

    assetTypes: List<KClass<Any>>
) : Serde<Any> {

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
    @SerdeProperties
    @Suppress("unused") // properties are used via AnnotationParameterArgumentResolver
    internal annotation class Properties(
        val assetTypes: Array<KClass<out Any>> = []
    )

    private val currentElementName = annotationProcessingContext.getCurrentElement().getName()

    private val serdes: Map<String, Serde<Any>> = assetTypes.associate { assetType ->
        val assetName = assetType.findAnnotation<Asset>()
            ?: error("${ConditionalSerde::class.simpleName} only supports classes that are annotated with '${Asset::class.simpleName}'. Found none on '${assetType.simpleName}'.")
        Pair(assetName.name, serdeFactory.getSerde(assetType))
    }

    override fun collectDataSections(data: Any): DataSection {
        val asset = data::class.findAnnotation<Asset>()
            ?: throw IllegalStateException("Could not find asset annotation for $currentElementName. Expected one of: ${serdes.keys}")
        val serde = serdes[asset.name]
            ?: throw IllegalStateException("Could not find serde for '${asset.name}' calculating byte count for $currentElementName. Expected one of: ${serdes.keys}")
        return serde.collectDataSections(data)
    }

    override fun serialize(outputStream: OutputStream, data: Any) {
        val assetName = serializationContext.peek().assetName
        val serde = serdes[assetName]
            ?: throw IllegalStateException("Could not find serde for '$assetName' writing $currentElementName. Expected one of: ${serdes.keys}")
        preProcessor.preProcess(data, serializationContext).let {
            serde.serialize(outputStream, it)
        }
    }

    override fun deserialize(inputStream: CountingInputStream): Any {
        val assetName = serializationContext.peek().assetName
        val serde = serdes[assetName]
            ?: throw InvalidDataException("Unexpected assetName '$assetName' reading $currentElementName. Expected one of: ${serdes.keys}")
        return serde.deserialize(inputStream).also {
            postProcessor.postProcess(it, serializationContext)
        }
    }
}
