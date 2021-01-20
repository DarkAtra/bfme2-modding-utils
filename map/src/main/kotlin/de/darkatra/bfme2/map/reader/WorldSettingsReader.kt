package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.MapFile
import org.apache.commons.io.input.CountingInputStream

class WorldSettingsReader(
	private val propertiesReader: PropertiesReader
) : AssetReader {

	companion object {
		const val ASSET_NAME = "WorldInfo"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {
		MapFileReader.readAsset(reader, context, ASSET_NAME) {
			builder.worldSettings(
				worldSettings = propertiesReader.read(reader, context)
			)
		}
	}
}
