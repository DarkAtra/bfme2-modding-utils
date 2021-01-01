package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readInt
import org.apache.commons.io.input.CountingInputStream
import java.nio.charset.StandardCharsets

class AssetProperty(
	val propertyKey: AssetPropertyKey,
	val value: Any
) {

	companion object : AssetReader<AssetProperty> {

		override fun read(reader: CountingInputStream, context: MapFileParseContext): AssetProperty {

			val propertyKey = AssetPropertyKey.read(reader, context)
			val value = when (propertyKey.propertyType) {
				AssetPropertyType.BOOLEAN -> reader.readBoolean()
				AssetPropertyType.INTEGER -> reader.readInt()
				AssetPropertyType.FLOAT -> reader.readInt()
				AssetPropertyType.ASCII_STRING -> reader.readNBytes(2).toString(StandardCharsets.US_ASCII)
				AssetPropertyType.UNICODE_STRING -> reader.readNBytes(2).toString(StandardCharsets.UTF_8)
				AssetPropertyType.UNKNOWN -> reader.readNBytes(2).toString(StandardCharsets.US_ASCII)
			}

			return AssetProperty(
				propertyKey = propertyKey,
				value = value
			)
		}
	}
}
