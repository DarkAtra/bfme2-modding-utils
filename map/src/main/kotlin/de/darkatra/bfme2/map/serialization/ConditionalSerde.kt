package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import java.io.OutputStream
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@UseSerdeProperties(ConditionalSerde.Properties::class)
internal class ConditionalSerde(
    serdeFactory: SerdeFactory,
    annotationProcessingContext: AnnotationProcessingContext,
    private val serializationContext: SerializationContext,
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

    override fun serialize(outputStream: OutputStream, data: Any) {
        TODO("Not yet implemented")
    }

    override fun deserialize(inputStream: CountingInputStream): Any {
        val assetName = serializationContext.peek().assetName
        val serde = serdes[assetName] ?: failWithException(assetName)
        return serde.deserialize(inputStream).also {
            postProcessor.postProcess(it, serializationContext)
        }
    }

    private fun failWithException(assetName: String): Nothing {
        throw InvalidDataException("Unexpected assetName '$assetName' reading $currentElementName. Expected one of: ${serdes.keys}")
    }
}
