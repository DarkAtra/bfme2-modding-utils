package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.Property
import de.darkatra.bfme2.readShort
import org.apache.commons.io.input.CountingInputStream

class PropertiesReader(
	private val propertyReader: PropertyReader
) {

	fun read(reader: CountingInputStream, context: MapFileParseContext): List<Property> {

		val numberOfProperties = reader.readShort()

		val properties = mutableListOf<Property>()
		for (i in 0 until numberOfProperties step 1) {
			properties.add(
				propertyReader.read(reader, context)
			)
		}

		return properties
	}
}
