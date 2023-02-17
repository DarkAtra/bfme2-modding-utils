package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.Vector2

@Asset(name = "StandingWaterAreas", version = 2u)
data class StandingWaterAreas(
    val areas: List<StandingWaterArea>
) {

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
}
