package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.Vector3

data class LookAtCameraAnimationFrame(
    override val frameIndex: UInt,
    override val interpolationType: CameraAnimationFrameInterpolationType,
    val position: Vector3,
    val roll: Float,
    val fieldOfView: Float
) : CameraAnimationFrame
