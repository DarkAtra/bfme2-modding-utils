package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.toLittleEndianInt
import org.apache.commons.io.input.CountingInputStream

class AssetList(
	val assetListItems: List<AssetListItem>
) : Asset {

	companion object : AssetReader<AssetList> {
		const val ASSET_NAME = "AssetList"

		override fun read(reader: CountingInputStream, context: MapFileParseContext): AssetList {

			return AssetReader.readAsset(reader, context) {

				val numberOfAssetListItems = reader.readNBytes(4).toLittleEndianInt()

				val assetListItems = mutableListOf<AssetListItem>()
				for (i in 0 until numberOfAssetListItems step 1) {
					assetListItems.add(
						AssetListItem.read(reader, context)
					)
				}

				context.mapHasAssetList = true

				AssetList(
					assetListItems = assetListItems
				)
			}
		}
	}
}
