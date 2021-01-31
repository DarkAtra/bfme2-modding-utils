package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException

enum class EvaluationIntervalType {
	OPERATION,
	MOVE_FORCES,
	BATTLE,
	UPKEEP,
	COMPLETE,
	ANY,
	FRAME_OR_SECONDS;

	companion object {

		fun ofUInt(uInt: UInt): EvaluationIntervalType {
			return when (uInt) {
				0u -> OPERATION
				1u -> MOVE_FORCES
				2u -> BATTLE
				3u -> UPKEEP
				4u -> COMPLETE
				5u -> ANY
				6u -> FRAME_OR_SECONDS
				else -> throw ConversionException("Unknown EvaluationIntervalType for uInt '$uInt'.")
			}
		}
	}
}
