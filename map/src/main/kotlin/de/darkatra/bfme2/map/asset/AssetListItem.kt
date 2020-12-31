package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.toLittleEndianInt
import org.apache.commons.io.input.CountingInputStream

data class AssetListItem(
	val typeId: Int,
	val instanceId: Int
) {

	companion object : AssetReader<AssetListItem> {

		override fun read(reader: CountingInputStream, context: MapFileParseContext): AssetListItem {

			return AssetListItem(
				typeId = reader.readNBytes(4).toLittleEndianInt(),
				instanceId = reader.readNBytes(4).toLittleEndianInt()
			)
		}
	}
}
