package de.darkatra.bfme2.v2.map.blendtile

import de.darkatra.bfme2.Vector2

data class CliffTextureMapping(
    val textureTile: UInt,
    val bottomLeftCoords: Vector2,
    val bottomRightCoords: Vector2,
    val topRightCoords: Vector2,
    val topLeftCoords: Vector2,
    val unknown2: UShort
)
