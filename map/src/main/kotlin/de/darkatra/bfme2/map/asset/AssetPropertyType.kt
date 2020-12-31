package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.ConversionException
import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.readByte
import org.apache.commons.io.input.CountingInputStream

enum class AssetPropertyType(
	private val byte: Byte
) {
	BOOLEAN(0),
	INTEGER(1),
	FLOAT(2),
	ASCII_STRING(3),
	UNICODE_STRING(4),

	// seems to be the same as ASCII_STRING
	UNKNOWN(5);

	companion object : AssetReader<AssetPropertyType> {

		override fun read(reader: CountingInputStream, context: MapFileParseContext): AssetPropertyType {

			val byte = reader.readByte()

			return values()
				.find { assetPropertyType -> assetPropertyType.byte == byte }
				?: throw ConversionException("Unknown AssetPropertyType for byte '$byte'.")
		}
	}
}
