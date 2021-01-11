package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException

enum class TileFlammability {
	FIRERESISTANT,
	GRASS,
	HIGHLYFLAMMABLE,
	UNDEFINED;

	companion object {

		fun ofByte(byte: Byte): TileFlammability {
			return when (byte) {
				0.toByte() -> FIRERESISTANT
				1.toByte() -> GRASS
				2.toByte() -> HIGHLYFLAMMABLE
				3.toByte() -> UNDEFINED
				else -> throw ConversionException("Unknown TileFlammability for byte '$byte'.")
			}
		}
	}
}
