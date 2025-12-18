package de.darkatra.bfme2

import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class Vector3(
    val x: Float,
    val y: Float,
    val z: Float
)
