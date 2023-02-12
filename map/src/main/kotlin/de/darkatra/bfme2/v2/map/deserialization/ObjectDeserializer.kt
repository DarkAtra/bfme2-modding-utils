package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializePostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.TypeArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.model.ConstructorParameter
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import de.darkatra.bfme2.v2.map.Asset as AssetAnnotation

class ObjectDeserializer<T : Any>(
    @Resolve(using = TypeArgumentResolver::class)
    val classOfT: KClass<T>,
    @Resolve(using = DeserializePostProcessorArgumentResolver::class)
    private val postProcessor: PostProcessor<T>
) : Deserializer<T> {

    private data class Asset(
        val assetVersion: UShort,
        val assetSize: UInt
    )

    override fun deserialize(inputStream: CountingInputStream, deserializationContext: DeserializationContext): T {

        val primaryConstructor = classOfT.primaryConstructor
            ?: error("${classOfT.simpleName} is required to have a primary constructor.")

        val currentAsset = classOfT.findAnnotation<AssetAnnotation>()?.let {
            Asset(
                assetVersion = inputStream.readUShort(),
                assetSize = inputStream.readUInt()
            )
        }

        val parameters = primaryConstructor.valueParameters

        val startPosition = inputStream.byteCount
        deserializationContext.setCurrentType(classOfT)

        val values = parameters.map { parameter ->

            deserializationContext.setCurrentParameter(parameter)

            deserializationContext.beginProcessing(
                ConstructorParameter(parameter)
            )
            val deserializer = deserializationContext.deserializerFactory.getDeserializer(deserializationContext)

            deserializer.deserialize(inputStream, deserializationContext).also {
                deserializationContext.endProcessingCurrentElement()
            }
        }

        if (currentAsset != null) {
            val currentEndPosition = inputStream.byteCount
            val expectedEndPosition = currentAsset.assetSize.toLong() + startPosition
            if (currentEndPosition != expectedEndPosition) {
                throw InvalidDataException("Error reading '${classOfT.simpleName}'. Expected reader to be at position $expectedEndPosition, but was at $currentEndPosition.")
            }
        }

        return primaryConstructor.call(*values.toTypedArray()).also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }
}
