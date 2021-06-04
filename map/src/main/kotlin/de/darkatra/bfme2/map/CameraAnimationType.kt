package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException

enum class CameraAnimationType {
	FREE,
	LOOK;

	companion object {
		fun ofName(name: String): CameraAnimationType {
			return values().find { it.name.equals(name, true) } ?: throw ConversionException("Unknown CameraAnimationType for name '$name'.")
		}
	}
}
