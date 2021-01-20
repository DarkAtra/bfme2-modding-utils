package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.MapFile
import org.apache.commons.io.input.CountingInputStream

class GlobalVersionReader : AssetReader {

	companion object {
		const val ASSET_NAME = "GlobalVersion"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {
		MapFileReader.readAsset(reader, context, ASSET_NAME) { version ->
			builder.globalVersion(version)
		}
	}
}
