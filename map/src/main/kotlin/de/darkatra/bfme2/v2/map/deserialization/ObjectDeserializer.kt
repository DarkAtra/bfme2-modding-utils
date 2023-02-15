package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import de.darkatra.bfme2.v2.map.Asset as AssetAnnotation

internal class ObjectDeserializer<T : Any>(
    annotationProcessingContext: AnnotationProcessingContext,
    private val classOfT: KClass<T>,
    private val deserializationContext: DeserializationContext,
    private val deserializers: List<Deserializer<*>>,
    private val postProcessor: PostProcessor<T>
) : Deserializer<T> {

    private val currentElementName = annotationProcessingContext.getCurrentElement().getName()

    override fun deserialize(inputStream: CountingInputStream): T {

        val primaryConstructor = classOfT.primaryConstructor
            ?: error("${classOfT.simpleName} is required to have a primary constructor.")

        val expectedAssetName = classOfT.findAnnotation<AssetAnnotation>()?.name
        val parameters = primaryConstructor.valueParameters

        if (expectedAssetName != null) {
            val currentAsset = deserializationContext.peek()
            if (expectedAssetName != currentAsset.assetName) {
                throw InvalidDataException("Unexpected assetName '${expectedAssetName}' reading $currentElementName. Expected: '${currentAsset.assetName}'")
            }
        }

        val values = parameters.mapIndexed { parameterIndex, parameter ->
            try {
                deserializers[parameterIndex].deserialize(inputStream)
            } catch (e: Exception) {
                throw InvalidDataException(
                    "Error deserializing value for '${classOfT.simpleName}#${parameter.name}' using ${deserializers[parameterIndex]::class.simpleName}.",
                    e
                )
            }
        }

        return primaryConstructor.call(*values.toTypedArray()).also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }
}
