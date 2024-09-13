package de.darkatra.bfme2.map.`object`

enum class RoadType(
    internal val uInt: UInt
) {
    NONE(0u),
    START(2u),
    END(4u),
    ANGLED(8u),
    BRIDGE_START(16u),
    BRIDGE_END(32u),
    TIGHT_CURVE(64u),
    END_CAP(128u),
    PRIMARY_TYPE(START.uInt or END.uInt or BRIDGE_START.uInt or BRIDGE_END.uInt),

    // TODO: find out all values and assign correct names
    UNKNOWN_10(10u),
    UNKNOWN_12(12u),
    UNKNOWN_24(24u),
    UNKNOWN_66(66u),
    UNKNOWN_68(68u),
    UNKNOWN_130(130u),
    UNKNOWN_132(132u),
    UNKNOWN_138(138u),
    UNKNOWN_140(140u),
    UNKNOWN_194(194u),
    UNKNOWN_196(196u),
    UNKNOWN_256(256u),
    UNKNOWN_258(258u),
    UNKNOWN_260(260u),
    UNKNOWN_512(512u)
}
