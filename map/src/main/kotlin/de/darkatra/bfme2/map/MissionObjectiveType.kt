package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException

@Suppress("unused")
enum class MissionObjectiveType(
	val uInt: UInt
) {
	ATTACK(0u),
	UNKNOWN_1(1u),
	UNKNOWN_2(2u),
	BUILD(3u),
	CAPTURE(4u),
	MOVE(5u),
	PROTECT(6u);

	companion object {
		fun ofUInt(uInt: UInt): MissionObjectiveType {
			return values().find { it.uInt == uInt } ?: throw ConversionException("Unknown MissionObjectiveType for uInt '$uInt'.")
		}
	}
}
