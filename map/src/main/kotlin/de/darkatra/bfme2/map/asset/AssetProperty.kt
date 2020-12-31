package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.toBoolean
import de.darkatra.bfme2.toLittleEndianFloat
import de.darkatra.bfme2.toLittleEndianInt
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
				AssetPropertyType.BOOLEAN -> reader.readByte().toBoolean()
				AssetPropertyType.INTEGER -> reader.readNBytes(4).toLittleEndianInt()
				AssetPropertyType.FLOAT -> reader.readNBytes(4).toLittleEndianFloat()
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
