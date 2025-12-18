package de.darkatra.bfme2

import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class Line2D(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float
)
