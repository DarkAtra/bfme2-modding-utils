package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.GlobalLighting
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.TimeOfDay
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import org.apache.commons.io.input.CountingInputStream
import java.awt.Color
import java.util.EnumMap

class GlobalLightingReader : AssetReader {

	private val globalLightingConfigurationReader = GlobalLightingConfigurationReader()

	companion object {
		const val ASSET_NAME = "GlobalLighting"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) { version ->

			val globalLightingBuilder = GlobalLighting.Builder()

			globalLightingBuilder.time(TimeOfDay.ofUInt(reader.readUInt()))

			globalLightingBuilder.lightingConfigurations(
				TimeOfDay.values().map { timeOfDay ->
					timeOfDay to globalLightingConfigurationReader.read(reader, version)
				}.toMap(EnumMap(TimeOfDay::class.java))
			)

			globalLightingBuilder.shadowColor(
				reader.readUInt().let {
					Color(
						((it shr 16) and 0xFFu).toInt(),
						((it shr 8) and 0xFFu).toInt(),
						(it and 0xFFu).toInt(),
						((it shr 24) and 0xFFu).toInt()
					)
				}
			)

			if (version >= 7u && version < 11u) {
				globalLightingBuilder.unknown(
					reader.readNBytes(if (version >= 9u) 4 else 44).toList()
				)
			}

			if (version >= 12u) {
				globalLightingBuilder.unknown2(
					Vector3(
						x = reader.readFloat(),
						y = reader.readFloat(),
						z = reader.readFloat()
					)
				)
				globalLightingBuilder.unknown3(
					reader.readUInt().let {
						Color(
							((it shr 16) and 0xFFu).toInt(),
							((it shr 8) and 0xFFu).toInt(),
							(it and 0xFFu).toInt(),
							((it shr 24) and 0xFFu).toInt()
						)
					}
				)
			}

			if (version >= 8u) {
				globalLightingBuilder.noCloudFactor(
					Vector3(
						x = reader.readFloat(),
						y = reader.readFloat(),
						z = reader.readFloat()
					)
				)
			}

			builder.globalLighting(globalLightingBuilder.build())
		}
	}
}