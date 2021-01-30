package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException

enum class TileFlammability {
	FIRE_RESISTANT,
	GRASS,
	HIGHLY_FLAMMABLE,
	UNDEFINED;

	companion object {

		fun ofByte(byte: Byte): TileFlammability {
			return when (byte) {
				0.toByte() -> FIRE_RESISTANT
				1.toByte() -> GRASS
				2.toByte() -> HIGHLY_FLAMMABLE
				3.toByte() -> UNDEFINED
				else -> throw ConversionException("Unknown TileFlammability for byte '$byte'.")
			}
		}
	}
}
