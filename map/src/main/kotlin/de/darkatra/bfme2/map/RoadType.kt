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
	UNKNOWN_1(10u),
	UNKNOWN_2(12u),
	UNKNOWN_3(66u),
	UNKNOWN_4(68u),
	UNKNOWN_5(130u),
	UNKNOWN_6(132u),
	UNKNOWN_7(138u),
	UNKNOWN_8(140u),
	UNKNOWN_9(194u),
	UNKNOWN_10(196u),
	UNKNOWN_11(256u),
	UNKNOWN_12(512u),
	PRIMARY_TYPE(START.uInt or END.uInt or BRIDGE_START.uInt or BRIDGE_END.uInt);

	companion object {
		fun ofUInt(uInt: UInt): RoadType {
			return values().find { it.uInt == uInt } ?: throw ConversionException("Unknown RoadType for uInt '$uInt'.")
		}
	}
}
