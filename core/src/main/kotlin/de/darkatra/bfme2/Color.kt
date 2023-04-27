package de.darkatra.bfme2

data class Color constructor(
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

    @Suppress("MemberVisibilityCanBePrivate") // public api
    val alpha: UInt
        get() = rgba shr 24 and 0xFFu

    init {
        if (red > 255u) {
            throw IllegalArgumentException("red outside of expected range: $red")
        }
        if (green > 255u) {
            throw IllegalArgumentException("green outside of expected range: $green")
        }
        if (blue > 255u) {
            throw IllegalArgumentException("blue outside of expected range: $blue")
        }
        if (alpha > 255u) {
            throw IllegalArgumentException("alpha outside of expected range: $alpha")
        }
    }
}
