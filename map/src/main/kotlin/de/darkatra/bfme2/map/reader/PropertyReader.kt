package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.Property
import de.darkatra.bfme2.map.PropertyType
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readShortPrefixedString
import de.darkatra.bfme2.readUInt
import org.apache.commons.io.input.CountingInputStream
import java.nio.charset.StandardCharsets

class PropertyReader(
	private val propertyKeyReader: PropertyKeyReader
) {

	fun read(reader: CountingInputStream, context: MapFileParseContext): Property {

		val propertyKey = propertyKeyReader.read(reader, context)

		val value = when (propertyKey.propertyType) {
			PropertyType.BOOLEAN -> reader.readBoolean()
			PropertyType.INTEGER -> reader.readUInt()
			PropertyType.FLOAT -> reader.readFloat()
			PropertyType.ASCII_STRING -> reader.readShortPrefixedString()
			PropertyType.UNICODE_STRING -> reader.readShortPrefixedString(StandardCharsets.UTF_8)
			PropertyType.UNKNOWN -> reader.readShortPrefixedString()
		}

		return Property(
			key = propertyKey,
			value = value
		)
	}
}
