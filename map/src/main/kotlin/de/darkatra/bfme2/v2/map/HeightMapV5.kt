package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.Deserializer
import de.darkatra.bfme2.v2.map.deserialization.UIntDeserializer
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

@Asset
data class HeightMapV5(
    val width: @Deserialize(using = UIntDeserializer::class, postProcessor = WidthPostProcessor::class) UInt,
    val height: @Deserialize(using = UIntDeserializer::class, postProcessor = HeightPostProcessor::class) UInt,
    val borderWidth: UInt,
    val borders: List<HeightMapBorder>,
    val elevations: @Deserialize(using = ElevationsDeserializer::class) Map<UInt, Map<UInt, UShort>>,
    val area: UInt
) {

    companion object {
        const val HEIGHT_MAP_WIDTH = "height-map-width"
        const val HEIGHT_MAP_HEIGHT = "height-map-height"
    }

    data class HeightMapBorder(
        val x: UInt,
        val y: UInt,
    )

    class WidthPostProcessor : PostProcessor<UInt> {
        override fun postProcess(data: UInt, deserializationContext: DeserializationContext) {
            deserializationContext.sharedData[HEIGHT_MAP_WIDTH] = data
        }
    }

    class HeightPostProcessor : PostProcessor<UInt> {
        override fun postProcess(data: UInt, deserializationContext: DeserializationContext) {
            deserializationContext.sharedData[HEIGHT_MAP_HEIGHT] = data
        }
    }

    class ElevationsDeserializer : Deserializer<Map<UInt, Map<UInt, UShort>>> {

        override fun deserialize(inputStream: CountingInputStream, deserializationContext: DeserializationContext): Map<UInt, Map<UInt, UShort>> {

            val width = deserializationContext.sharedData[HEIGHT_MAP_WIDTH] as UInt
            val height = deserializationContext.sharedData[HEIGHT_MAP_HEIGHT] as UInt

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
