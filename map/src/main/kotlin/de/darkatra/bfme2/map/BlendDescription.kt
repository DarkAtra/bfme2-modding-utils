package de.darkatra.bfme2.map

data class BlendDescription(
    val secondaryTextureTile: UInt,
    val rawBlendDirection: List<Byte>,
    val blendDirection: BlendDirection,
    val flags: BlendFlags,
    val twoSided: Boolean,
    val magicValue1: UInt,
    val magicValue2: UInt
)
