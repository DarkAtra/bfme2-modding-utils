package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.Vector3

data class Camera(
    val lookAtPoint: Vector3,
    val name: String,
    val pitch: Float,
    val roll: Float,
    val yaw: Float,
    val zoom: Float,
    val fieldOfView: Float,
    val unknown: Float
)
