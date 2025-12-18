package de.darkatra.bfme2.map.heightmap

import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class HeightMapBorder(
    val x: UInt,
    val y: UInt,
)
