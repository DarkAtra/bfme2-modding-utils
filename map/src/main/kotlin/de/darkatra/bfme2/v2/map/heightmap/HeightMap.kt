package de.darkatra.bfme2.v2.map.heightmap

import com.google.common.collect.Table
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.HeightMapDependentMapDeserializer
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializersArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcess
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.SharedDataProvidingPostProcessor

@Asset(name = "HeightMapData", version = 5u)
@PostProcess(using = HeightMap.HeightMapPostProcessor::class)
data class HeightMap(
    val width: @PostProcess(using = HeightMapWidthPostProcessor::class) UInt,
    val height: @PostProcess(using = HeightMapHeightPostProcessor::class) UInt,
    val borderWidth: @PostProcess(using = HeightMapBorderWidthPostProcessor::class) UInt,
    val borders: List<HeightMapBorder>,
    val area: @PostProcess(using = HeightMapAreaPostProcessor::class) UInt,
    val elevations:
    @Deserialize(using = HeightMapDependentMapDeserializer::class)
    Table<@DeserializersArgumentResolver.Ignore UInt, @DeserializersArgumentResolver.Ignore UInt, UShort>
) {

    internal companion object {
        const val HEIGHT_MAP_WIDTH = "height-map-width"
        const val HEIGHT_MAP_HEIGHT = "height-map-height"
        const val HEIGHT_MAP_AREA = "height-map-area"
        const val HEIGHT_MAP_BORDER_WIDTH = "height-map-border-width"
    }

    internal class HeightMapPostProcessor : PostProcessor<HeightMap> {
        override fun postProcess(data: HeightMap, context: DeserializationContext) {
            if (data.width * data.height != data.area) {
                throw InvalidDataException("Width (${data.width}) times height (${data.height}) does not equal to the area (${data.area}).")
            }
        }
    }

    internal class HeightMapWidthPostProcessor : SharedDataProvidingPostProcessor<UInt>(HEIGHT_MAP_WIDTH)
    internal class HeightMapHeightPostProcessor : SharedDataProvidingPostProcessor<UInt>(HEIGHT_MAP_HEIGHT)
    internal class HeightMapAreaPostProcessor : SharedDataProvidingPostProcessor<UInt>(HEIGHT_MAP_AREA)
    internal class HeightMapBorderWidthPostProcessor : SharedDataProvidingPostProcessor<UInt>(HEIGHT_MAP_BORDER_WIDTH)
}
