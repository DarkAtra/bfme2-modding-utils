package de.darkatra.bfme2.map.water

import de.darkatra.bfme2.Vector2
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class StandingWaterArea(
    val id: UInt,
    val name: String,
    val layerName: String,
    val uvScrollSpeed: Float,
    val useAdditiveBlending: Boolean,
    val bumpMapTexture: String,
    val skyTexture: String,
    val point: List<Vector2>,
    val waterHeight: UInt,
    val fxShader: String,
    val depthColor: String
)
