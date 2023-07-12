package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.Vector4

sealed interface CameraAnimationFrame {
    val frameIndex: UInt
    val interpolationType: CameraAnimationFrameInterpolationType
}

data class FreeCameraAnimationFrame(
    override val frameIndex: UInt,
    override val interpolationType: CameraAnimationFrameInterpolationType,
    val position: Vector3,
    val rotation: Vector4,
    val fieldOfView: Float
) : CameraAnimationFrame

data class LookAtCameraAnimationFrame(
    override val frameIndex: UInt,
    override val interpolationType: CameraAnimationFrameInterpolationType,
    val position: Vector3,
    val roll: Float,
    val fieldOfView: Float
) : CameraAnimationFrame

data class LookAtTargetCameraAnimationFrame(
    override val frameIndex: UInt,
    override val interpolationType: CameraAnimationFrameInterpolationType,
    val lookAt: Vector3
) : CameraAnimationFrame
