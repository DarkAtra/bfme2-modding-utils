package de.darkatra.bfme2.map.writer

import de.darkatra.bfme2.getByteCount
import de.darkatra.bfme2.map.AssetListItem
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.writeUInt
import org.apache.commons.io.output.CountingOutputStream

class AssetListWriter : AssetWriter {

	override fun write(writer: CountingOutputStream, context: MapFileComposeContext, mapFile: MapFile) {

		if (mapFile.assetList == null) {
			return
		}

		writer.writeUInt(context.getOrCreateAssetIndex(AssetName.ASSET_LIST.assetName))

		val assetList = mapFile.assetList
		val assetSize = determineAssetSize(assetList)

		MapFileWriter.writeAsset(writer, 1u, assetSize) {

			writer.writeUInt(assetList.size.toUInt())

			mapFile.assetList.forEach { assetListItem ->
				writer.writeUInt(assetListItem.typeId)
				writer.writeUInt(assetListItem.instanceId)
			}
		}
	}

	override fun composeAssetNames(context: MapFileComposeContext, mapFile: MapFile) {

		if (mapFile.assetList == null) {
			return
		}

		context.getOrCreateAssetIndex(AssetName.ASSET_LIST.assetName)
	}

	private fun determineAssetSize(assetList: List<AssetListItem>): UInt {
		return assetList.size.toUInt().getByteCount().plus(
			assetList.sumOf { it.typeId.getByteCount() + it.instanceId.getByteCount() }
		)
	}
}
