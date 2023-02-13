package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.Deserializer
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializationContextResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcess
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

@Asset
@PostProcess(using = HeightMapV5.HeightMapPostProcessor::class)
data class HeightMapV5(
    val width: @PostProcess(using = WidthPostProcessor::class) UInt,
    val height: @PostProcess(using = HeightPostProcessor::class) UInt,
    val borderWidth: @PostProcess(using = BorderWidthPostProcessor::class) UInt,
    val borders: List<HeightMapBorder>,
    val area: @PostProcess(using = AreaPostProcessor::class) UInt,
    val elevations: @Deserialize(using = ElevationsDeserializer::class) Map<UInt, Map<UInt, UShort>>
) {

    data class HeightMapBorder(
        val x: UInt,
        val y: UInt,
    )

    internal companion object {
        const val HEIGHT_MAP_WIDTH = "height-map-width"
        const val HEIGHT_MAP_HEIGHT = "height-map-height"
        const val HEIGHT_MAP_AREA = "height-map-area"
        const val HEIGHT_MAP_BORDER_WIDTH = "height-map-border-width"
    }

    internal class HeightMapPostProcessor : PostProcessor<HeightMapV5> {
        override fun postProcess(data: HeightMapV5, context: DeserializationContext) {
            if (data.width * data.height != data.area) {
                throw InvalidDataException("Width (${data.width}) times height (${data.height}) does not equal to the area (${data.area}).")
            }
        }
    }

    internal class WidthPostProcessor : PostProcessor<UInt> {
        override fun postProcess(data: UInt, context: DeserializationContext) {
            context.sharedData[HEIGHT_MAP_WIDTH] = data
        }
    }

    internal class HeightPostProcessor : PostProcessor<UInt> {
        override fun postProcess(data: UInt, context: DeserializationContext) {
            context.sharedData[HEIGHT_MAP_HEIGHT] = data
        }
    }

    internal class AreaPostProcessor : PostProcessor<UInt> {
        override fun postProcess(data: UInt, context: DeserializationContext) {
            context.sharedData[HEIGHT_MAP_AREA] = data
        }
    }

    internal class BorderWidthPostProcessor : PostProcessor<UInt> {
        override fun postProcess(data: UInt, context: DeserializationContext) {
            context.sharedData[HEIGHT_MAP_BORDER_WIDTH] = data
        }
    }

    internal class ElevationsDeserializer(
        @Resolve(DeserializationContextResolver::class)
        private val context: DeserializationContext
    ) : Deserializer<Map<UInt, Map<UInt, UShort>>> {

        override fun deserialize(inputStream: CountingInputStream): Map<UInt, Map<UInt, UShort>> {

            val width = context.sharedData[HEIGHT_MAP_WIDTH] as UInt
            val height = context.sharedData[HEIGHT_MAP_HEIGHT] as UInt

            val elevations = mutableMapOf<UInt, MutableMap<UInt, UShort>>()
            for (x in 0u until width step 1) {
                elevations[x] = mutableMapOf()
                for (y in 0u until height step 1) {
                    elevations[x]!![y] = inputStream.readUShort()
                }
            }

            return elevations
        }
    }
}
