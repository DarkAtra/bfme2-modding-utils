package de.darkatra.bfme2.map.globallighting

import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_METHODS)
enum class TimeOfDay(
    internal val uInt: UInt
) {
    MORNING(1u),
    AFTERNOON(2u),
    EVENING(3u),
    NIGHT(4u)
}
