package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.map.MapFileReader
import de.darkatra.bfme2.map.Property
import de.darkatra.bfme2.map.PropertyKey
import de.darkatra.bfme2.map.PropertyType
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readShort
import de.darkatra.bfme2.toLittleEndianInt
import org.apache.commons.io.input.CountingInputStream
import java.nio.charset.StandardCharsets

class WorldSettingsReader : AssetReader {

	companion object {
		const val ASSET_NAME = "WorldInfo"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) {

			val numberOfProperties = reader.readShort()

			val worldSettings = mutableListOf<Property>()
			for (i in 0 until numberOfProperties step 1) {
				worldSettings.add(
					readProperty(reader, context)
				)
			}

			builder.worldSettings(
				worldSettings = worldSettings
			)
		}
	}

	// TODO: this should probably be moved to another class
	private fun readProperty(reader: CountingInputStream, context: MapFileParseContext): Property {

		val propertyKey = readPropertyKey(reader, context)

		val value = when (propertyKey.propertyType) {
			PropertyType.BOOLEAN -> reader.readBoolean()
			PropertyType.INTEGER -> reader.readInt()
			PropertyType.FLOAT -> reader.readInt()
			PropertyType.ASCII_STRING -> reader.readNBytes(2).toString(StandardCharsets.US_ASCII)
			PropertyType.UNICODE_STRING -> reader.readNBytes(2).toString(StandardCharsets.UTF_8)
			PropertyType.UNKNOWN -> reader.readNBytes(2).toString(StandardCharsets.US_ASCII)
		}

		return Property(
			key = propertyKey,
			value = value
		)
	}

	// TODO: this should probably be moved to another class
	private fun readPropertyKey(reader: CountingInputStream, context: MapFileParseContext): PropertyKey {

		val propertyType = PropertyType.ofByte(reader.readByte())

		val nameIndex = reader.readNBytes(3).toLittleEndianInt()
		val name = context.getAssetName(nameIndex)

		return PropertyKey(
			propertyType = propertyType,
			name = name
		)
	}
}
