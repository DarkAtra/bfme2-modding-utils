package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.BlendDescription
import de.darkatra.bfme2.map.BlendTileTexture
import de.darkatra.bfme2.map.CliffTextureMapping
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.TileFlammability
import de.darkatra.bfme2.read2DByteArrayAsMap
import de.darkatra.bfme2.read2DSageBooleanArray
import de.darkatra.bfme2.read2DUIntArrayAsMap
import de.darkatra.bfme2.read2DUShortArrayAsMap
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.to2DUIntArrayAsMap
import org.apache.commons.io.input.CountingInputStream

class BlendTileDataReader(
	private val blendDescriptionReader: BlendDescriptionReader = BlendDescriptionReader(),
	private val blendTileTextureReader: BlendTileTextureReader = BlendTileTextureReader(),
	private val cliffTextureMappingReader: CliffTextureMappingReader = CliffTextureMappingReader()
) : AssetReader {

	companion object {
		const val ASSET_NAME = "BlendTileData"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) { version ->

			if (version < 6u) {
				throw InvalidDataException("Unexpected version '$version' for $ASSET_NAME.")
			}

			if (context.heightMap == null) {
				throw InvalidDataException("Expected HeightMap to be read before BlendTileData.")
			}

			val width = context.heightMap!!.width
			val height = context.heightMap!!.height
			val area = context.heightMap!!.area
			val borderWidth = context.heightMap!!.borderWidth

			val numberOfTiles = reader.readUInt()
			if (numberOfTiles != area) {
				throw InvalidDataException("Expected the number of bend tiles to be equal to the height maps area.")
			}

			// TODO: read as Map<UInt, Map<UInt, Short>>
			val tiles = reader.read2DUShortArrayAsMap(width, height)

			val isUsing32BitBlendsAndCliffs = version in 14u until 23u

			val blends = when (isUsing32BitBlendsAndCliffs) {
				true -> reader.read2DUIntArrayAsMap(width, height)
				false -> reader.read2DUShortArrayAsMap(width, height).to2DUIntArrayAsMap()
			}
			val threeWayBlends = when (isUsing32BitBlendsAndCliffs) {
				true -> reader.read2DUIntArrayAsMap(width, height)
				false -> reader.read2DUShortArrayAsMap(width, height).to2DUIntArrayAsMap()
			}
			val cliffTextures = when (isUsing32BitBlendsAndCliffs) {
				true -> reader.read2DUIntArrayAsMap(width, height)
				false -> reader.read2DUShortArrayAsMap(width, height).to2DUIntArrayAsMap()
			}

			if (version > 6u) {

				var passabilityWidth = width
				if (version == 7.toUShort()) {
					// if the border width is large enough to fully contain the clipped data.
					if (passabilityWidth % 8u <= 6u && passabilityWidth % 8u <= borderWidth) {
						passabilityWidth -= passabilityWidth % 8u
					}
				}

				// If terrain is passable, there's a 0 in the data file.
				val impassability = reader.read2DSageBooleanArray(passabilityWidth, height)
			}

			if (version >= 10u) {
				val impassabilityToPlayers = reader.read2DSageBooleanArray(width, height)
			}

			if (version >= 11u) {
				val passageWidths = reader.read2DSageBooleanArray(width, height)
			}

			if (version in 14u until 24u) {
				val taintability = reader.read2DSageBooleanArray(width, height)
			}

			if (version >= 15u) {
				val extraPassability = reader.read2DSageBooleanArray(width, height)
			}

			if (version in 16u until 24u) {
				val flammability = reader.read2DByteArrayAsMap(width, height, TileFlammability::ofByte)
			}

			if (version >= 17u) {
				val visibility = reader.read2DSageBooleanArray(width, height)
			}

			if (version >= 24u) {
				// TODO: are these in the right order?
				val buildability = reader.read2DSageBooleanArray(width, height)
				val impassabilityToAirUnits = reader.read2DSageBooleanArray(width, height)
				val tiberiumGrowability = reader.read2DSageBooleanArray(width, height)
			}

			if (version >= 25u) {
				val dynamicShrubberyDensity = reader.read2DByteArrayAsMap(width, height)
			}

			val textureCellCount = reader.readUInt()

			// TODO: why are we subtracting 1?
			val blendsCount = reader.readUInt().let {
				when {
					it > 0u -> it - 1u
					else -> it
				}
			}

			// TODO: why are we subtracting 1?
			val cliffBlendsCount = reader.readUInt().let {
				when {
					it > 0u -> it - 1u
					else -> it
				}
			}

			val numberOfTextures = reader.readUInt()
			val textures = mutableListOf<BlendTileTexture>()
			for (i in 0u until numberOfTextures step 1) {
				textures.add(
					blendTileTextureReader.read(reader)
				)
			}

			// Can be a variety of values, don't know what it means.
			val magicValue1 = reader.readUInt()
			val magicValue2 = reader.readUInt()

			if (magicValue2 != 0u) {
				throw InvalidDataException("Expected magic value 2 to be 0.")
			}

			val blendDescriptions = mutableListOf<BlendDescription>()
			for (i in 0u until blendsCount step 1) {
				blendDescriptions.add(
					blendDescriptionReader.read(reader)
				)
			}

			val cliffTextureMappings = mutableListOf<CliffTextureMapping>()
			for (i in 0u until cliffBlendsCount step 1) {
				cliffTextureMappings.add(
					cliffTextureMappingReader.read(reader)
				)
			}
		}
	}
}
