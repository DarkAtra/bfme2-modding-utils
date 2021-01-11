package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector2

data class CliffTextureMapping(
	val textureTile: Int,
	val bottomLeftCoords: Vector2,
	val bottomRightCoords: Vector2,
	val topRightCoords: Vector2,
	val topLeftCoords: Vector2,
	val unknown2: Short
)
