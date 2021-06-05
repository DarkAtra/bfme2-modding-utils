package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException

@Suppress("unused")
enum class RoadType(
	val uInt: UInt
) {
	NONE(0u),
	START(2u),
	END(4u),
	ANGLED(8u),
	BRIDGE_START(16u),
	BRIDGE_END(32u),
	TIGHT_CURVE(64u),
	END_CAP(128u),
	UNKNOWN_3(256u),
	UNKNOWN_4(512u),
	PRIMARY_TYPE(START.uInt or END.uInt or BRIDGE_START.uInt or BRIDGE_END.uInt);

	companion object {
		fun ofUInt(uInt: UInt): RoadType {
			return values().find { it.uInt == uInt } ?: throw ConversionException("Unknown RoadType for uInt '$uInt'.")
		}
	}
}
