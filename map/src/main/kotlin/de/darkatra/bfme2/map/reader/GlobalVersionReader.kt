package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import org.apache.commons.io.input.CountingInputStream

class GlobalVersionReader : AssetReader {

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {
		MapFileReader.readAsset(reader, context, AssetName.GLOBAL_VERSION.assetName) { version ->
			builder.globalVersion(version)
		}
	}
}
