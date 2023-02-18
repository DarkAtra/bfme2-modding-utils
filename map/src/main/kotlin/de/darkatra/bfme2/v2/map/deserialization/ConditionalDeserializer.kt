package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@UseDeserializerProperties(ConditionalDeserializer.Properties::class)
internal class ConditionalDeserializer(
    deserializerFactory: DeserializerFactory,
    annotationProcessingContext: AnnotationProcessingContext,
    private val deserializationContext: DeserializationContext,
    private val postProcessor: PostProcessor<Any>,

    assetTypes: List<KClass<Any>>
) : Deserializer<Any> {

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
    @DeserializerProperties
    @Suppress("unused") // properties are used via AnnotationParameterArgumentResolver
    internal annotation class Properties(
        val assetTypes: Array<KClass<out Any>> = []
    )

    private val currentElementName = annotationProcessingContext.getCurrentElement().getName()

    private val deserializers: Map<String, Deserializer<Any>> = assetTypes.associate { assetType ->
        val assetName = assetType.findAnnotation<Asset>()
            ?: error("${ConditionalDeserializer::class.simpleName} only supports classes that are annotated with '${Asset::class.simpleName}.")
        Pair(assetName.name, deserializerFactory.getDeserializer(assetType))
    }

    override fun deserialize(inputStream: CountingInputStream): Any {
        val assetName = deserializationContext.peek().assetName
        val deserializer = deserializers[assetName] ?: failWithException(assetName)
        return deserializer.deserialize(inputStream).also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }

    private fun failWithException(assetName: String): Nothing {
        throw InvalidDataException("Unexpected assetName '$assetName' reading $currentElementName. Expected one of: ${deserializers.keys}")
    }
}
