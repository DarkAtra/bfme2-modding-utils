package de.darkatra.bfme2.map.globallighting

enum class TimeOfDay(
    internal val uInt: UInt
) {
    MORNING(1u),
    AFTERNOON(2u),
    EVENING(3u),
    NIGHT(4u)
}
