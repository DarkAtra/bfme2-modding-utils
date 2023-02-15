package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import de.darkatra.bfme2.v2.map.Asset as AssetAnnotation

internal class ObjectDeserializer<T : Any>(
    private val classOfT: KClass<T>,
    private val deserializationContext: DeserializationContext,
    private val deserializers: List<Deserializer<*>>,
    private val postProcessor: PostProcessor<T>
) : Deserializer<T> {

    private data class Asset(
        val assetName: String,
        val assetVersion: UShort,
        val assetSize: UInt
    )

    override fun deserialize(inputStream: CountingInputStream): T {

        val primaryConstructor = classOfT.primaryConstructor
            ?: error("${classOfT.simpleName} is required to have a primary constructor.")

        val currentAsset = classOfT.findAnnotation<AssetAnnotation>()?.let { assetAnnotation ->
            Asset(
                assetName = assetAnnotation.name,
                assetVersion = inputStream.readUShort(),
                assetSize = inputStream.readUInt()
            )
        }

        val parameters = primaryConstructor.valueParameters

        val startPosition = inputStream.byteCount

        if (currentAsset != null) {
            val endPosition = currentAsset.assetSize.toLong() + startPosition
            deserializationContext.push(currentAsset.assetName, endPosition)
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

        if (currentAsset != null) {
            deserializationContext.pop()

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
