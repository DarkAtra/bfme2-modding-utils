package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector3

data class Skybox(
    val position: Vector3,
    val scale: Float,
    val rotation: Float,
    val textureScheme: String
)
