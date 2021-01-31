package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.GlobalWaterSettings
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readFloat
import org.apache.commons.io.input.CountingInputStream
import kotlin.experimental.or

class GlobalWaterSettingsReader : AssetReader {

	companion object {
		const val ASSET_NAME = "GlobalWaterSettings"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) {

			val reflectionEnabled = reader.readBoolean()

			val unused = reader.readNBytes(3).reduce { acc, byte -> acc or byte }
			if (unused != 0.toByte()) {
				throw InvalidDataException("Unexpected non empty bytes after boolean found.")
			}

			builder.globalWaterSettings(
				GlobalWaterSettings(
					reflectionEnabled = reflectionEnabled,
					reflectionPlaneZ = reader.readFloat()
				)
			)
		}
	}
}
