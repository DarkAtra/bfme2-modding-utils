package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.map.MapFileParseContext
import org.apache.commons.io.input.CountingInputStream

class GlobalVersion(
	val version: Short
) : Asset {

	companion object : AssetReader<GlobalVersion> {
		const val ASSET_NAME = "GlobalVersion"

		override fun read(reader: CountingInputStream, context: MapFileParseContext): GlobalVersion {

			return AssetReader.readAsset(reader, context) { version ->
				GlobalVersion(
					version = version
				)
			}
		}
	}
}
