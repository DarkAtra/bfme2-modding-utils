package de.darkatra.bfme2.map.writer

import de.darkatra.bfme2.getByteCount
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.BlendTileData
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.writeUInt
import org.apache.commons.io.output.CountingOutputStream

class BlendTileDataWriter : AssetWriter {

	override fun write(writer: CountingOutputStream, context: MapFileComposeContext, mapFile: MapFile) {

		// TODO: currently hardcoded
		val version = 6u.toUShort()

		writer.writeUInt(context.getOrCreateAssetIndex(AssetName.BLEND_TILE_DATA.assetName))

		val blendTileData = mapFile.blendTileData
		val assetSize = determineAssetSize(blendTileData, version)

		MapFileWriter.writeAsset(writer, version, assetSize) {

		}
	}

	override fun composeAssetNames(context: MapFileComposeContext, mapFile: MapFile) {
		context.getOrCreateAssetIndex(AssetName.BLEND_TILE_DATA.assetName)
	}

	private fun determineAssetSize(blendTileData: BlendTileData, version: UShort): UInt {
		return listOf(
			blendTileData.tiles.size.toUInt().getByteCount()
		).sum()
	}
}
