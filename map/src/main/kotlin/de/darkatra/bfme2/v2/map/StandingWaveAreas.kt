package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.Vector2

@Asset(name = "StandingWaveAreas", version = 2u)
data class StandingWaveAreas(
    val areas: List<StandingWaveArea>
) {

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
        val enablePcaWave: Boolean,
        val waveParticleFXName: String
    )
}
