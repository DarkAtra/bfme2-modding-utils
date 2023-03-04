package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.Vector4

data class FreeCameraAnimationFrame(
    val frameIndex: UInt,
    val interpolationType: CameraAnimationFrameInterpolationType,
    val position: Vector3,
    val rotation: Vector4,
    val fieldOfView: Float
)
