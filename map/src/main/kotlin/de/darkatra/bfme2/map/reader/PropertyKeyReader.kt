package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.PropertyKey
import de.darkatra.bfme2.map.PropertyType
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.toLittleEndianInt
import org.apache.commons.io.input.CountingInputStream

class PropertyKeyReader {

	fun read(reader: CountingInputStream, context: MapFileParseContext): PropertyKey {

		val propertyType = PropertyType.ofByte(reader.readByte())

		val nameIndex = reader.readNBytes(3).toLittleEndianInt()
		val name = context.getAssetName(nameIndex)

		return PropertyKey(
			propertyType = propertyType,
			name = name
		)
	}
}
