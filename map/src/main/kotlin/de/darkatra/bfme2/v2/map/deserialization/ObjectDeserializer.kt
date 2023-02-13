package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializersArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.PostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.model.Class
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import de.darkatra.bfme2.v2.map.Asset as AssetAnnotation

internal class ObjectDeserializer<T : Any>(
    private val classOfT: KClass<T>,
    private val context: DeserializationContext,
    private val deserializers: List<Deserializer<*>>,
    private val postProcessor: PostProcessor<T>
) : Deserializer<T> {

    @Suppress("UNCHECKED_CAST")
    internal constructor(classOfT: KClass<T>, context: DeserializationContext) : this(
        classOfT,
        context,
        DeserializersArgumentResolver(context).resolve(Class(classOfT)).also {
            context.reset()
        },
        PostProcessorArgumentResolver().resolve(Class(classOfT)) as PostProcessor<T>
    )

    private data class Asset(
        val assetVersion: UShort,
        val assetSize: UInt
    )

    override fun deserialize(inputStream: CountingInputStream): T {

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
        context.setCurrentType(classOfT)

        val values = List(parameters.size) { parameterIndex ->
            deserializers[parameterIndex].deserialize(inputStream)
        }

        if (currentAsset != null) {
            val currentEndPosition = inputStream.byteCount
            val expectedEndPosition = currentAsset.assetSize.toLong() + startPosition
            if (currentEndPosition != expectedEndPosition) {
                throw InvalidDataException("Error reading '${classOfT.simpleName}'. Expected reader to be at position $expectedEndPosition, but was at $currentEndPosition.")
            }
        }

        return primaryConstructor.call(*values.toTypedArray()).also {
            postProcessor.postProcess(it, context)
        }
    }
}
