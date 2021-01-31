package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.FogSettings
import de.darkatra.bfme2.map.MapFile
import org.apache.commons.io.input.CountingInputStream

class FogSettingsReader : AssetReader {

	companion object {
		const val ASSET_NAME = "FogSettings"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {
		MapFileReader.readAsset(reader, context, ASSET_NAME) {
			builder.fogSettings(
				FogSettings(
					unknown = reader.readNBytes(24).toList()
				)
			)
		}
	}
}
