package de.darkatra.bfme2.v2.map

import com.google.common.collect.Table
import de.darkatra.bfme2.ConversionException
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.Vector2
import de.darkatra.bfme2.v2.map.deserialization.BlendCountDeserializer
import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.HeightMapDependentMapDeserializer
import de.darkatra.bfme2.v2.map.deserialization.HeightMapDependentMapDeserializer.Mode
import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.UShortPrefixedStringDeserializer
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializersArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcess
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.SharedDataProvidingPostProcessor
import kotlin.experimental.or

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

    // TODO: implement validator
    data class BlendDescription(
        val secondaryTextureTile: UInt,
        private val rawBlendDirection: @ListDeserializer.Properties(mode = ListDeserializer.Mode.FIXED, size = 4u) List<Byte>,
        val flags: BlendFlags,
        val twoSided: Boolean,
        val magicValue1: UInt,
        val magicValue2: UInt
    ) {

        @Suppress("unused") // public api
        val blendDirection: BlendDirection
            get() {
                val bytes = rawBlendDirection.toTypedArray()
                var result: Byte = 0
                for (i in bytes.indices) {
                    if (bytes[i] != 0.toByte() && bytes[i] != 1.toByte()) {
                        throw NotImplementedError("BlendDirection conversion not fully implemented yet.")
                    }
                    if (bytes[i] != 0.toByte()) {
                        result = result or (bytes[i].toInt() shl i).toByte()
                    }
                }
                return BlendDirection.ofByte(result)
            }

        enum class BlendDirection(
            val byte: Byte
        ) {
            BLEND_TOWARDS_RIGHT(1),     // Or towards left, if BlendDescription.Flags contains Flipped
            BLEND_TOWARDS_TOP(2),       // Or towards bottom, if BlendDescription.Flags contains Flipped
            BLEND_TOWARDS_TOP_RIGHT(4), // Or towards bottom left, if BlendDescription.Flags contains Flipped
            BLEND_TOWARDS_TOP_LEFT(8);  // Or towards bottom right, if BlendDescription.Flags contains Flipped

            companion object {
                fun ofByte(byte: Byte): BlendDirection {
                    return values().find { it.byte == byte } ?: throw ConversionException("Unknown BlendDirection for byte '$byte'.")
                }
            }
        }

        enum class BlendFlags(
            val byte: Byte
        ) {
            NONE(0),
            FLIPPED(1),
            ALSO_HAS_BOTTOM_LEFT_OR_TOP_RIGHT_BLEND(2),
            FLIPPED_ALSO_HAS_BOTTOM_LEFT_OR_TOP_RIGHT_BLEND(FLIPPED.byte or ALSO_HAS_BOTTOM_LEFT_OR_TOP_RIGHT_BLEND.byte);

            companion object {
                fun ofByte(byte: Byte): BlendFlags {
                    return values().find { it.byte == byte } ?: throw ConversionException("Unknown BlendFlags for byte '$byte'.")
                }
            }
        }
    }

    @PostProcess(using = BlendTileTexture.BlendTileTexturePostProcessor::class)
    data class BlendTileTexture(
        val cellStart: UInt,
        /**
         * Texture "cells" are 64x64 blocks within a source texture.
         * So for example, a 128x128 texture has 4 cells.
         */
        val cellCount: UInt,
        /**
         * Size of this texture, in texture cell units.
         */
        val cellSize: UInt,
        val magicValue: UInt,
        val name: @Deserialize(using = UShortPrefixedStringDeserializer::class) String
    ) {

        internal class BlendTileTexturePostProcessor : PostProcessor<BlendTileTexture> {
            override fun postProcess(data: BlendTileTexture, context: DeserializationContext) {
                if (data.cellSize * data.cellSize != data.cellCount) {
                    throw InvalidDataException("Expected cellCount '${data.cellCount}' to equal cellSize times cellSize (${data.cellSize} * ${data.cellSize}).")
                }
                if (data.magicValue != 0u) {
                    throw InvalidDataException("Expected magicValue to be zero.")
                }
            }
        }
    }

    data class CliffTextureMapping(
        val textureTile: UInt,
        val bottomLeftCoords: Vector2,
        val bottomRightCoords: Vector2,
        val topRightCoords: Vector2,
        val topLeftCoords: Vector2,
        val unknown2: UShort
    )

    enum class TileFlammability(
        val byte: Byte
    ) {
        FIRE_RESISTANT(0),
        GRASS(1),
        HIGHLY_FLAMMABLE(2),
        UNDEFINED(3);

        companion object {
            fun ofByte(byte: Byte): TileFlammability {
                return values().find { it.byte == byte } ?: throw ConversionException("Unknown TileFlammability for byte '$byte'.")
            }
        }
    }

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
