package de.darkatra.bfme2.v2.map.blendtile

import com.google.common.collect.Table
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.deserialization.BlendCountDeserializer
import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.HeightMapDependentMapDeserializer
import de.darkatra.bfme2.v2.map.deserialization.HeightMapDependentMapDeserializer.Mode
import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializersArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcess
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.SharedDataProvidingPostProcessor

@Asset(name = "BlendTileData", version = 18u)
@PostProcess(using = BlendTileData.BlendTileDataPostProcessor::class)
data class BlendTileData(
    val numberOfTiles: UInt,
    val tiles:
    @Deserialize(using = HeightMapDependentMapDeserializer::class)
    Table<@DeserializersArgumentResolver.Ignore UInt, @DeserializersArgumentResolver.Ignore UInt, UShort>,
    val blends:
    @Deserialize(using = HeightMapDependentMapDeserializer::class)
    Table<@DeserializersArgumentResolver.Ignore UInt, @DeserializersArgumentResolver.Ignore UInt, UInt>,
    val threeWayBlends:
    @Deserialize(using = HeightMapDependentMapDeserializer::class)
    Table<@DeserializersArgumentResolver.Ignore UInt, @DeserializersArgumentResolver.Ignore UInt, UInt>,
    val cliffTextures:
    @Deserialize(using = HeightMapDependentMapDeserializer::class)
    Table<@DeserializersArgumentResolver.Ignore UInt, @DeserializersArgumentResolver.Ignore UInt, UInt>,
    val impassability:
    @HeightMapDependentMapDeserializer.Properties(mode = Mode.SAGE_BOOLEAN)
    @Deserialize(using = HeightMapDependentMapDeserializer::class)
    Table<@DeserializersArgumentResolver.Ignore UInt, @DeserializersArgumentResolver.Ignore UInt, Boolean>,
    val impassabilityToPlayers:
    @HeightMapDependentMapDeserializer.Properties(mode = Mode.SAGE_BOOLEAN)
    @Deserialize(using = HeightMapDependentMapDeserializer::class)
    Table<@DeserializersArgumentResolver.Ignore UInt, @DeserializersArgumentResolver.Ignore UInt, Boolean>,
    val passageWidths:
    @HeightMapDependentMapDeserializer.Properties(mode = Mode.SAGE_BOOLEAN)
    @Deserialize(using = HeightMapDependentMapDeserializer::class)
    Table<@DeserializersArgumentResolver.Ignore UInt, @DeserializersArgumentResolver.Ignore UInt, Boolean>,
    val taintability:
    @HeightMapDependentMapDeserializer.Properties(mode = Mode.SAGE_BOOLEAN)
    @Deserialize(using = HeightMapDependentMapDeserializer::class)
    Table<@DeserializersArgumentResolver.Ignore UInt, @DeserializersArgumentResolver.Ignore UInt, Boolean>,
    val extraPassability:
    @HeightMapDependentMapDeserializer.Properties(mode = Mode.SAGE_BOOLEAN)
    @Deserialize(using = HeightMapDependentMapDeserializer::class)
    Table<@DeserializersArgumentResolver.Ignore UInt, @DeserializersArgumentResolver.Ignore UInt, Boolean>,
    val flammability:
    @Deserialize(using = HeightMapDependentMapDeserializer::class)
    Table<@DeserializersArgumentResolver.Ignore UInt, @DeserializersArgumentResolver.Ignore UInt, TileFlammability>,
    val visibility:
    @HeightMapDependentMapDeserializer.Properties(mode = Mode.SAGE_BOOLEAN)
    @Deserialize(using = HeightMapDependentMapDeserializer::class)
    Table<@DeserializersArgumentResolver.Ignore UInt, @DeserializersArgumentResolver.Ignore UInt, Boolean>,
    val textureCellCount: UInt,
    private val blendsCount:
    @PostProcess(using = BlendCountPostProcessor::class)
    @Deserialize(using = BlendCountDeserializer::class)
    UInt,
    private val cliffBlendsCount:
    @PostProcess(using = CliffBlendCountPostProcessor::class)
    @Deserialize(using = BlendCountDeserializer::class)
    UInt,
    val textures: List<BlendTileTexture>,
    val magicValue1: UInt,
    val magicValue2: UInt,
    val blendDescriptions:
    @ListDeserializer.Properties(mode = ListDeserializer.Mode.SHARED_DATA, sharedDataKey = BLEND_COUNT)
    List<BlendDescription>,
    val cliffTextureMappings:
    @ListDeserializer.Properties(mode = ListDeserializer.Mode.SHARED_DATA, sharedDataKey = CLIFF_BLEND_COUNT)
    List<CliffTextureMapping>
) {

    internal companion object {
        const val BLEND_COUNT = "blend-count"
        const val CLIFF_BLEND_COUNT = "cliff-blend-count"
    }

    internal class BlendTileDataPostProcessor : PostProcessor<BlendTileData> {
        override fun postProcess(data: BlendTileData, context: DeserializationContext) {
            if (data.magicValue2 != 0u) {
                throw InvalidDataException("Expected magicValue2 to be zero.")
            }
        }
    }

    internal class BlendCountPostProcessor : SharedDataProvidingPostProcessor<UInt>(BLEND_COUNT)
    internal class CliffBlendCountPostProcessor : SharedDataProvidingPostProcessor<UInt>(CLIFF_BLEND_COUNT)
}
