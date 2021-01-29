package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.BlendDescription
import de.darkatra.bfme2.readUInt
import org.apache.commons.io.input.CountingInputStream

class BlendDescriptionReader {

	fun read(reader: CountingInputStream): BlendDescription {

		val secondaryTextureTile = reader.readUInt()

		val rawBlendDirection = reader.readNBytes(4)
		// TODO: to blend direction
		// val blendDirection =

		reader.skip(1) // TODO: dont skip
		// val flags: BlendFlags = reader.ReadByte() as BlendFlags

		reader.skip(1) // TODO: dont skip
		// val twoSided = reader.readBoolean()

		// TODO: What is this?
		val magicValue1 = reader.readUInt()
		// if (magicValue1 != MAGIC_VALUE_1_1 && magicValue1 != MAGIC_VALUE_1_2) {
		// 	throw InvalidDataException("Expected magic value 1 to equal '$MAGIC_VALUE_1_1' or '$MAGIC_VALUE_1_2'.")
		// }

		// TODO: What is this?
		val magicValue2 = reader.readUInt()
		// if (magicValue2 != MAGIC_VALUE_2) {
		// 	throw InvalidDataException("Expected magic value 2 to equal '$MAGIC_VALUE_2'.")
		// }

		return BlendDescription()
	}
}
