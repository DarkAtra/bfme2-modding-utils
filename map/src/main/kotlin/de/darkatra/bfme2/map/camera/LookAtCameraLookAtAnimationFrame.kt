package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.Vector3

// TODO: is this really a CameraAnimationFrame?
data class LookAtCameraLookAtAnimationFrame(
    override val frameIndex: UInt,
    override val interpolationType: CameraAnimationFrameInterpolationType,
    val lookAt: Vector3
) : CameraAnimationFrame
