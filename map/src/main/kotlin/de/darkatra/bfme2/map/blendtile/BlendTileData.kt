package de.darkatra.bfme2.map.blendtile

import com.google.common.collect.Table
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.*
import de.darkatra.bfme2.map.serialization.HeightMapDependentMapSerde.Mode
import de.darkatra.bfme2.map.serialization.argumentresolution.SerdesArgumentResolver
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcess
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.postprocessing.SharedDataProvidingPostProcessor

@Asset(name = "BlendTileData", version = 18u)
@PostProcess(using = BlendTileData.BlendTileDataPostProcessor::class)
data class BlendTileData(
    val numberOfTiles: UInt,
    val tiles:
    @Serialize(using = HeightMapDependentMapSerde::class)
    Table<@SerdesArgumentResolver.Ignore UInt, @SerdesArgumentResolver.Ignore UInt, UShort>,
    val blends:
    @Serialize(using = HeightMapDependentMapSerde::class)
    Table<@SerdesArgumentResolver.Ignore UInt, @SerdesArgumentResolver.Ignore UInt, UInt>,
    val threeWayBlends:
    @Serialize(using = HeightMapDependentMapSerde::class)
    Table<@SerdesArgumentResolver.Ignore UInt, @SerdesArgumentResolver.Ignore UInt, UInt>,
    val cliffTextures:
    @Serialize(using = HeightMapDependentMapSerde::class)
    Table<@SerdesArgumentResolver.Ignore UInt, @SerdesArgumentResolver.Ignore UInt, UInt>,
    val impassability:
    @HeightMapDependentMapSerde.Properties(mode = Mode.SAGE_BOOLEAN)
    @Serialize(using = HeightMapDependentMapSerde::class)
    Table<@SerdesArgumentResolver.Ignore UInt, @SerdesArgumentResolver.Ignore UInt, Boolean>,
    val impassabilityToPlayers:
    @HeightMapDependentMapSerde.Properties(mode = Mode.SAGE_BOOLEAN)
    @Serialize(using = HeightMapDependentMapSerde::class)
    Table<@SerdesArgumentResolver.Ignore UInt, @SerdesArgumentResolver.Ignore UInt, Boolean>,
    val passageWidths:
    @HeightMapDependentMapSerde.Properties(mode = Mode.SAGE_BOOLEAN)
    @Serialize(using = HeightMapDependentMapSerde::class)
    Table<@SerdesArgumentResolver.Ignore UInt, @SerdesArgumentResolver.Ignore UInt, Boolean>,
    val taintability:
    @HeightMapDependentMapSerde.Properties(mode = Mode.SAGE_BOOLEAN)
    @Serialize(using = HeightMapDependentMapSerde::class)
    Table<@SerdesArgumentResolver.Ignore UInt, @SerdesArgumentResolver.Ignore UInt, Boolean>,
    val extraPassability:
    @HeightMapDependentMapSerde.Properties(mode = Mode.SAGE_BOOLEAN)
    @Serialize(using = HeightMapDependentMapSerde::class)
    Table<@SerdesArgumentResolver.Ignore UInt, @SerdesArgumentResolver.Ignore UInt, Boolean>,
    val flammability:
    @Serialize(using = HeightMapDependentMapSerde::class)
    Table<@SerdesArgumentResolver.Ignore UInt, @SerdesArgumentResolver.Ignore UInt, TileFlammability>,
    val visibility:
    @HeightMapDependentMapSerde.Properties(mode = Mode.SAGE_BOOLEAN)
    @Serialize(using = HeightMapDependentMapSerde::class)
    Table<@SerdesArgumentResolver.Ignore UInt, @SerdesArgumentResolver.Ignore UInt, Boolean>,
    val textureCellCount: UInt,
    internal val blendsCount:
    @PostProcess(using = BlendCountPostProcessor::class)
    @Serialize(using = BlendCountSerde::class)
    UInt,
    internal val cliffBlendsCount:
    @PostProcess(using = CliffBlendCountPostProcessor::class)
    @Serialize(using = BlendCountSerde::class)
    UInt,
    val textures: List<BlendTileTexture>,
    val magicValue1: UInt,
    val magicValue2: UInt,
    val blendDescriptions:
    @ListSerde.Properties(mode = ListSerde.Mode.SHARED_DATA, sharedDataKey = BLEND_COUNT)
    List<BlendDescription>,
    val cliffTextureMappings:
    @ListSerde.Properties(mode = ListSerde.Mode.SHARED_DATA, sharedDataKey = CLIFF_BLEND_COUNT)
    List<CliffTextureMapping>
) {

    internal companion object {
        const val BLEND_COUNT = "blend-count"
        const val CLIFF_BLEND_COUNT = "cliff-blend-count"
    }

    internal class BlendTileDataPostProcessor : PostProcessor<BlendTileData> {
        override fun postProcess(data: BlendTileData, context: SerializationContext) {
            if (data.magicValue2 != 0u) {
                throw InvalidDataException("Expected magicValue2 to be zero.")
            }
        }
    }

    internal class BlendCountPostProcessor : SharedDataProvidingPostProcessor<UInt>(BLEND_COUNT)
    internal class CliffBlendCountPostProcessor : SharedDataProvidingPostProcessor<UInt>(CLIFF_BLEND_COUNT)
}
