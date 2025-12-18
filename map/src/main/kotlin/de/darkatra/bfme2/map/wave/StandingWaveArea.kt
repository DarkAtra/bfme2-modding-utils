package de.darkatra.bfme2.map.wave

import de.darkatra.bfme2.Vector2
import de.darkatra.bfme2.map.serialization.Serialize
import de.darkatra.bfme2.map.serialization.UIntBooleanSerde
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class StandingWaveArea(
    val id: UInt,
    val name: String,
    val layerName: String,
    val uvScrollSpeed: Float,
    val useAdditiveBlending: Boolean,
    val points: List<Vector2>,
    val unknown: UInt,
    val finalWidth: UInt,
    val finalHeight: UInt,
    val initialWidthFraction: UInt,
    val initialHeightFraction: UInt,
    val initialVelocity: UInt,
    val timeToFade: UInt,
    val timeToCompress: UInt,
    val timeOffset2ndWave: UInt,
    val distanceFromShore: UInt,
    val texture: String,
    val enablePcaWave: @Serialize(using = UIntBooleanSerde::class) Boolean
)
