package de.darkatra.bfme2.map.writer

import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.writeUInt
import de.darkatra.bfme2.writeUShort
import org.apache.commons.io.output.CountingOutputStream

class GlobalVersionWriter : AssetWriter {

	override fun write(writer: CountingOutputStream, context: MapFileComposeContext, mapFile: MapFile) {

		if (mapFile.globalVersion == null) {
			return
		}

		writer.writeUInt(context.getOrCreateAssetIndex(AssetName.GLOBAL_VERSION.assetName))

		MapFileWriter.writeAsset(writer, 1u, 2u) {
			writer.writeUShort(mapFile.globalVersion)
		}
	}

	override fun composeAssetNames(context: MapFileComposeContext, mapFile: MapFile) {

		if (mapFile.globalVersion == null) {
			return
		}

		context.getOrCreateAssetIndex(AssetName.GLOBAL_VERSION.assetName)
	}
}
