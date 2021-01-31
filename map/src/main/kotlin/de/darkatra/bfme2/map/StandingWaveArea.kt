package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector2

data class StandingWaveArea(
	val id: UInt,
	val name: String,
	val layerName: String,
	val uvScrollSpeed: Float,
	val useAdditiveBlending: Boolean,
	val point: List<Vector2>,
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
