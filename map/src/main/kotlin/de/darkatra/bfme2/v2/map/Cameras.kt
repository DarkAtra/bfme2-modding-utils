package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.Vector3

@Asset(name = "NamedCameras", version = 2u)
data class Cameras(
    val cameras: List<Camera>
) {

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
}
