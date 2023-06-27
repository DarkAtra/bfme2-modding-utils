package de.darkatra.bfme2.map.riverarea

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.Line2D
import de.darkatra.bfme2.map.serialization.FourByteColorSerde
import de.darkatra.bfme2.map.serialization.Serialize

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
    val color: @Serialize(using = FourByteColorSerde::class) Color,
    val alpha: Float,
    val waterHeight: UInt,
    val minimumWaterLod: String,
    val lines: List<Line2D>
)
