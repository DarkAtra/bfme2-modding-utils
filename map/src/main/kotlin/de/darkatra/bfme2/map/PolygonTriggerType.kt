package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException

@Suppress("unused")
enum class PolygonTriggerType(
	val uShort: UShort
) {
	AREA(0u),
	WATER(1u),
	RIVER(256u),
	WATER_AND_RIVER(WATER.uShort or RIVER.uShort);

	companion object {
		fun ofUShort(uShort: UShort): PolygonTriggerType {
			return values().find { it.uShort == uShort } ?: throw ConversionException("Unknown PolygonTriggerType for uShort '$uShort'.")
		}
	}
}
