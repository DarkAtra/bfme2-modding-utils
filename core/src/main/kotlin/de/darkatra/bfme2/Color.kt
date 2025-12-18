package de.darkatra.bfme2

import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class Color(
    val rgba: UInt
) {

    constructor(
        red: UInt,
        green: UInt,
        blue: UInt,
        alpha: UInt = 255u
    ) : this(
        (alpha and 0xFFu shl 24) or
            (red and 0xFFu shl 16) or
            (green and 0xFFu shl 8) or
            (blue and 0xFFu shl 0)
    )

    val red: UInt
        get() = rgba shr 16 and 0xFFu

    val green: UInt
        get() = rgba shr 8 and 0xFFu

    val blue: UInt
        get() = rgba shr 0 and 0xFFu

    @PublicApi
    val alpha: UInt
        get() = rgba shr 24 and 0xFFu

    init {
        require(red <= 255u) {
            "red outside of expected range: $red"
        }
        require(green <= 255u) {
            "green outside of expected range: $green"
        }
        require(blue <= 255u) {
            "blue outside of expected range: $blue"
        }
        require(alpha <= 255u) {
            "alpha outside of expected range: $alpha"
        }
    }
}
