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

		fun ofInt(int: Int): EvaluationIntervalType {
			return when (int) {
				0 -> OPERATION
				1 -> MOVE_FORCES
				2 -> BATTLE
				3 -> UPKEEP
				4 -> COMPLETE
				5 -> ANY
				6 -> FRAME_OR_SECONDS
				else -> throw ConversionException("Unknown EvaluationIntervalType for int '$int'.")
			}
		}
	}
}
