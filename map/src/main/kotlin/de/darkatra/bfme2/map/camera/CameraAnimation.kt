package de.darkatra.bfme2.map.camera

data class CameraAnimation(
    val animationType: CameraAnimationType,
    val name: String,
    val numberOfFrames: UInt,
    val startOffset: UInt,
    val cameraFrames: List<FreeCameraAnimationFrame>
)
