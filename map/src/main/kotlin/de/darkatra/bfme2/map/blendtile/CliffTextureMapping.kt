package de.darkatra.bfme2.map.blendtile

import de.darkatra.bfme2.Vector2
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class CliffTextureMapping(
    val textureTile: UInt,
    val bottomLeftCoords: Vector2,
    val bottomRightCoords: Vector2,
    val topRightCoords: Vector2,
    val topLeftCoords: Vector2,
    val unknown2: UShort
)
