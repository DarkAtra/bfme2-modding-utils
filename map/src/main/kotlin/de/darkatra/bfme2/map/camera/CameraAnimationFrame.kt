package de.darkatra.bfme2.map.camera

sealed interface CameraAnimationFrame {
    val frameIndex: UInt
    val interpolationType: CameraAnimationFrameInterpolationType
}
