package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.BlendDescription
import de.darkatra.bfme2.map.BlendTileTexture
import de.darkatra.bfme2.map.CliffTextureMapping
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.TileFlammability
import de.darkatra.bfme2.read2DByteArray
import de.darkatra.bfme2.read2DIntArray
import de.darkatra.bfme2.read2DSageBooleanArray
import de.darkatra.bfme2.read2DShortArray
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.to2DIntArray
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

			if (version < 6.toUShort()) {
				throw InvalidDataException("Unexpected version '$version' for $ASSET_NAME.")
			}

			if (context.heightMap == null) {
				throw InvalidDataException("Expected HeightMap to be read before BlendTileData.")
			}

			val width = context.heightMap!!.width
			val height = context.heightMap!!.height
			val area = context.heightMap!!.area
			val borderWidth = context.heightMap!!.borderWidth

			val numberOfTiles = reader.readInt()
			if (numberOfTiles != area) {
				throw InvalidDataException("Expected the number of bend tiles to be equal to the height maps area.")
			}

			val tiles = reader.read2DShortArray(width, height)

			val isUsing32BitBlendsAndCliffs = version in 14.toUShort() until 23.toUShort()

			val blends = when (isUsing32BitBlendsAndCliffs) {
				true -> reader.read2DIntArray(width, height)
				false -> reader.read2DShortArray(width, height).to2DIntArray()
			}
			val threeWayBlends = when (isUsing32BitBlendsAndCliffs) {
				true -> reader.read2DIntArray(width, height)
				false -> reader.read2DShortArray(width, height).to2DIntArray()
			}
			val cliffTextures = when (isUsing32BitBlendsAndCliffs) {
				true -> reader.read2DIntArray(width, height)
				false -> reader.read2DShortArray(width, height).to2DIntArray()
			}

			if (version > 6.toUShort()) {

				var passabilityWidth = width
				if (version == 7.toUShort()) {
					// if the border width is large enough to fully contain the clipped data.
					if (passabilityWidth % 8 <= 6 && passabilityWidth % 8 <= borderWidth) {
						passabilityWidth -= passabilityWidth % 8
					}
				}

				// If terrain is passable, there's a 0 in the data file.
				val impassability = reader.read2DSageBooleanArray(passabilityWidth, height)
			}

			if (version >= 10.toUShort()) {
				val impassabilityToPlayers = reader.read2DSageBooleanArray(width, height)
			}

			if (version >= 11.toUShort()) {
				val passageWidths = reader.read2DSageBooleanArray(width, height)
			}

			if (version in 14.toUShort() until 24.toUShort()) {
				val taintability = reader.read2DSageBooleanArray(width, height)
			}

			if (version >= 15.toUShort()) {
				val extraPassability = reader.read2DSageBooleanArray(width, height)
			}

			if (version in 16.toUShort() until 24.toUShort()) {
				val flammability = reader.read2DByteArray(width, height, TileFlammability::ofByte)
			}

			if (version >= 17.toUShort()) {
				val visibility = reader.read2DSageBooleanArray(width, height)
			}

			if (version >= 24.toUShort()) {
				// TODO: are these in the right order?
				val buildability = reader.read2DSageBooleanArray(width, height)
				val impassabilityToAirUnits = reader.read2DSageBooleanArray(width, height)
				val tiberiumGrowability = reader.read2DSageBooleanArray(width, height)
			}

			if (version >= 25.toUShort()) {
				val dynamicShrubberyDensity = reader.read2DByteArray(width, height)
			}

			val textureCellCount = reader.readInt()

			// TODO: why are we subtracting 1?
			val blendsCount = reader.readInt().let {
				when {
					it > 0 -> it - 1
					else -> it
				}
			}

			// TODO: why are we subtracting 1?
			val cliffBlendsCount = reader.readInt().let {
				when {
					it > 0 -> it - 1
					else -> it
				}
			}

			val numberOfTextures = reader.readInt()
			val textures = mutableListOf<BlendTileTexture>()
			for (i in 0 until numberOfTextures step 1) {
				textures.add(
					blendTileTextureReader.read(reader)
				)
			}

			// Can be a variety of values, don't know what it means.
			val magicValue1 = reader.readInt()
			val magicValue2 = reader.readInt()

			if (magicValue2 != 0) {
				throw InvalidDataException("Expected magic value 2 to be 0.")
			}

			val blendDescriptions = mutableListOf<BlendDescription>()
			for (i in 0 until blendsCount step 1) {
				blendDescriptions.add(
					blendDescriptionReader.read(reader)
				)
			}

			val cliffTextureMappings = mutableListOf<CliffTextureMapping>()
			for (i in 0 until cliffBlendsCount step 1) {
				cliffTextureMappings.add(
					cliffTextureMappingReader.read(reader)
				)
			}
		}
	}
}
