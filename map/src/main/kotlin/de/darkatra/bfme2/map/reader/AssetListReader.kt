package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.AssetListItem
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readInt
import org.apache.commons.io.input.CountingInputStream

class AssetListReader : AssetReader {

	companion object {
		const val ASSET_NAME = "AssetList"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) {

			val numberOfAssetListItems = reader.readInt()

			val assetList = mutableListOf<AssetListItem>()
			for (i in 0 until numberOfAssetListItems step 1) {

				val typeId = reader.readInt()
				val instanceId = reader.readInt()

				assetList.add(
					AssetListItem(
						typeId = typeId,
						instanceId = instanceId
					)
				)
			}

			context.mapHasAssetList = true

			builder.assetList(
				assetList = assetList
			)
		}
	}
}
