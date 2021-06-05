package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException

@Suppress("unused")
enum class EvaluationIntervalType(
	val uInt: UInt
) {
	OPERATION(0u),
	MOVE_FORCES(1u),
	BATTLE(2u),
	UPKEEP(3u),
	COMPLETE(4u),
	ANY(5u),
	FRAME_OR_SECONDS(6u);

	companion object {
		fun ofUInt(uInt: UInt): EvaluationIntervalType {
			return values().find { it.uInt == uInt } ?: throw ConversionException("Unknown EvaluationIntervalType for uInt '$uInt'.")
		}
	}
}
