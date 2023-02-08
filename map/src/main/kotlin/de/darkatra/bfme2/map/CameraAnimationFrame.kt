package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.Vector4

abstract class CameraAnimationFrame(
    open val frameIndex: UInt,
    open val interpolationType: CameraAnimationFrameInterpolationType
)

data class FreeCameraAnimationCameraFrame(
    val position: Vector3,
    val rotation: Vector4,
    val fieldOfView: Float,
    override val frameIndex: UInt,
    override val interpolationType: CameraAnimationFrameInterpolationType
) : CameraAnimationFrame(frameIndex, interpolationType)

data class LookAtCameraAnimationCameraFrame(
    val position: Vector3,
    val roll: Float,
    val fieldOfView: Float,
    override val frameIndex: UInt,
    override val interpolationType: CameraAnimationFrameInterpolationType
) : CameraAnimationFrame(frameIndex, interpolationType)

data class LookAtCameraAnimationLookAtFrame(
    val lookAt: Vector3,
    override val frameIndex: UInt,
    override val interpolationType: CameraAnimationFrameInterpolationType
) : CameraAnimationFrame(frameIndex, interpolationType)
