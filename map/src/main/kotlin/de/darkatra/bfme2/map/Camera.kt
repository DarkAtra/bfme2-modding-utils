package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector3

data class Camera(
    val lookAtPoint: Vector3,
    val name: String,
    val pitch: Float,
    val roll: Float,
    val yaw: Float,
    val zoom: Float,
    val fieldOfView: Float,
    // TODO: what is this used for?
    val unknown: Float
)
