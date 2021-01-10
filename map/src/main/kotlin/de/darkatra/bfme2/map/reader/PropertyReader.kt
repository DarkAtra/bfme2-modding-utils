package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.Property
import de.darkatra.bfme2.map.PropertyType
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readInt
import org.apache.commons.io.input.CountingInputStream
import java.nio.charset.StandardCharsets

class PropertyReader(
	private val propertyKeyReader: PropertyKeyReader
) {

	fun read(reader: CountingInputStream, context: MapFileParseContext): Property {

		val propertyKey = propertyKeyReader.read(reader, context)

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
}
