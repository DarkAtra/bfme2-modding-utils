package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException

enum class CameraAnimationFrameInterpolationType {
    CATM,
    LINE;

    companion object {
        fun ofName(name: String): CameraAnimationFrameInterpolationType {
            return values().find { it.name.equals(name, true) } ?: throw ConversionException("Unknown CameraAnimationFrameInterpolationType for name '$name'.")
        }
    }
}
