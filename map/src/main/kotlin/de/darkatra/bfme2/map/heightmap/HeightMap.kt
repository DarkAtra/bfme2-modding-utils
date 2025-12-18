package de.darkatra.bfme2.map.heightmap

import com.google.common.collect.Table
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.HeightMapDependentMapSerde
import de.darkatra.bfme2.map.serialization.SerializationContext
import de.darkatra.bfme2.map.serialization.Serialize
import de.darkatra.bfme2.map.serialization.argumentresolution.SerdesArgumentResolver
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcess
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.postprocessing.SharedDataProvidingPostProcessor
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "HeightMapData", version = 5u)
@PostProcess(using = HeightMap.HeightMapPostProcessor::class)
data class HeightMap(
    val width: @PostProcess(using = HeightMapWidthPostProcessor::class) UInt,
    val height: @PostProcess(using = HeightMapHeightPostProcessor::class) UInt,
    val borderWidth: @PostProcess(using = HeightMapBorderWidthPostProcessor::class) UInt,
    val borders: List<HeightMapBorder>,
    val area: @PostProcess(using = HeightMapAreaPostProcessor::class) UInt,
    val elevations:
    @Serialize(using = HeightMapDependentMapSerde::class)
    Table<@SerdesArgumentResolver.Ignore UInt, @SerdesArgumentResolver.Ignore UInt, UShort>
) {

    internal companion object {
        const val HEIGHT_MAP_WIDTH = "height-map-width"
        const val HEIGHT_MAP_HEIGHT = "height-map-height"
        const val HEIGHT_MAP_AREA = "height-map-area"
        const val HEIGHT_MAP_BORDER_WIDTH = "height-map-border-width"
    }

    @ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
    internal class HeightMapPostProcessor : PostProcessor<HeightMap> {
        override fun postProcess(data: HeightMap, context: SerializationContext) {
            if (data.width * data.height != data.area) {
                throw InvalidDataException("Width (${data.width}) times height (${data.height}) does not equal to the area (${data.area}).")
            }
        }
    }

    @ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
    internal class HeightMapWidthPostProcessor : SharedDataProvidingPostProcessor<UInt>(HEIGHT_MAP_WIDTH)

    @ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
    internal class HeightMapHeightPostProcessor : SharedDataProvidingPostProcessor<UInt>(HEIGHT_MAP_HEIGHT)

    @ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
    internal class HeightMapAreaPostProcessor : SharedDataProvidingPostProcessor<UInt>(HEIGHT_MAP_AREA)

    @ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
    internal class HeightMapBorderWidthPostProcessor : SharedDataProvidingPostProcessor<UInt>(HEIGHT_MAP_BORDER_WIDTH)
}
