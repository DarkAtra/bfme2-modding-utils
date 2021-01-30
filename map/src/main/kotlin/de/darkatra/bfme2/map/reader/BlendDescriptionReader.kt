package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.BlendDescription
import de.darkatra.bfme2.map.BlendDirection
import de.darkatra.bfme2.map.BlendFlags
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readUInt
import org.apache.commons.io.input.CountingInputStream
import kotlin.experimental.or


class BlendDescriptionReader {

	companion object {
		private const val MAGIC_VALUE_1_1 = 0xFFFFFFFFu
		private const val MAGIC_VALUE_1_2 = 24u
		private const val MAGIC_VALUE_2 = 0x7ADA0000u
	}

	fun read(reader: CountingInputStream): BlendDescription {

		val secondaryTextureTile = reader.readUInt()

		val rawBlendDirection = reader.readNBytes(4)
		val blendDirection = toBlendDirection(rawBlendDirection)
		val flags: BlendFlags = BlendFlags.ofByte(reader.readByte())
		val twoSided = reader.readBoolean()

		val magicValue1 = reader.readUInt()
		if (magicValue1 != MAGIC_VALUE_1_1 && magicValue1 != MAGIC_VALUE_1_2) {
			throw InvalidDataException("Expected magic value 1 to equal '$MAGIC_VALUE_1_1' or '$MAGIC_VALUE_1_2'.")
		}

		val magicValue2 = reader.readUInt()
		if (magicValue2 != MAGIC_VALUE_2) {
			throw InvalidDataException("Expected magic value 2 to equal '$MAGIC_VALUE_2'.")
		}

		return BlendDescription(
			secondaryTextureTile = secondaryTextureTile,
			rawBlendDirection = rawBlendDirection.asList(),
			blendDirection = blendDirection,
			flags = flags,
			twoSided = twoSided,
			magicValue1 = magicValue1,
			magicValue2 = magicValue2
		)
	}

	private fun toBlendDirection(bytes: ByteArray): BlendDirection {
		var result: Byte = 0
		for (i in bytes.indices) {
			if (bytes[i] != 0.toByte() && bytes[i] != 1.toByte()) {
				throw NotImplementedError("BlendDirection conversion not fully implemented yet.")
			}
			if (bytes[i] != 0.toByte()) {
				result = result or (bytes[i].toInt() shl i).toByte()
			}
		}
		return BlendDirection.ofByte(result)
	}
}
