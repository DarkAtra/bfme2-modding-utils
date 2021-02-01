package de.darkatra.bfme2.map

import java.awt.Color
import java.awt.geom.Line2D

data class RiverArea(
	val id: UInt,
	val name: String,
	val layerName: String,
	val uvScrollSpeed: Float,
	val useAdditiveBlending: Boolean,
	val riverTexture: String,
	val noiseTexture: String,
	val alphaEdgeTexture: String,
	val sparkleTexture: String,
	val color: Color,
	val alpha: Float,
	val waterHeight: UInt,
	val riverType: String?,
	val minimumWaterLod: String,
	val lines: List<Line2D>
)
