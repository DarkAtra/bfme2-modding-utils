package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException

enum class TimeOfDay(
    val uInt: UInt
) {
    MORNING(1u),
    AFTERNOON(2u),
    EVENING(3u),
    NIGHT(4u);

    companion object {
        fun ofUInt(uInt: UInt): TimeOfDay {
            return values().find { it.uInt == uInt } ?: throw ConversionException("Unknown TimeOfDay for uInt '$uInt'.")
        }
    }
}
