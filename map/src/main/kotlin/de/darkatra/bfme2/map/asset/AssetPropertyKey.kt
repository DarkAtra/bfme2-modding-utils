package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.toLittleEndianInt
import org.apache.commons.io.input.CountingInputStream

data class AssetPropertyKey(
	val propertyType: AssetPropertyType,
	val name: String
) {

	companion object : AssetReader<AssetPropertyKey> {

		override fun read(reader: CountingInputStream, context: MapFileParseContext): AssetPropertyKey {

			val propertyType = AssetPropertyType.read(reader, context)

			val nameIndex = reader.readNBytes(3).toLittleEndianInt()
			val name = context.getAssetName(nameIndex)

			return AssetPropertyKey(
				propertyType = propertyType,
				name = name
			)
		}
	}
}
