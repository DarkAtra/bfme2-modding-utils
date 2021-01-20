package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException

enum class SequentialScriptTarget {
	TEAM,
	UNIT;

	companion object {

		fun ofByte(byte: Byte): SequentialScriptTarget {
			return when (byte) {
				0.toByte() -> TEAM
				1.toByte() -> UNIT
				else -> throw ConversionException("Unknown SequentialScriptTarget for byte '$byte'.")
			}
		}
	}
}
