package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.Line2D
import de.darkatra.bfme2.v2.map.deserialization.ByteColorDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize

@Asset(name = "RiverAreas", version = 2u)
data class RiverAreas(
    val areas: List<RiverArea>
) {

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
        val color: @Deserialize(using = ByteColorDeserializer::class) Color,
        val alpha: Float,
        val waterHeight: UInt,
        val minimumWaterLod: String,
        val lines: List<Line2D>
    )
}
