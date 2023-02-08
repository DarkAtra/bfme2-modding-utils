package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.AssetListItem
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readUInt
import org.apache.commons.io.input.CountingInputStream

class AssetListReader : AssetReader {

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

        MapFileReader.readAsset(reader, context, AssetName.ASSET_LIST.assetName) {

            val numberOfAssetListItems = reader.readUInt()

            val assetList = mutableListOf<AssetListItem>()
            for (i in 0u until numberOfAssetListItems step 1) {

                val typeId = reader.readUInt()
                val instanceId = reader.readUInt()

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
