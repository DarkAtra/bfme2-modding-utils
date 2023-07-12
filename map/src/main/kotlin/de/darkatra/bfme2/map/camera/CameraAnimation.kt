package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.map.serialization.CameraAnimationSerde
import de.darkatra.bfme2.map.serialization.Serialize

@Serialize(using = CameraAnimationSerde::class)
sealed interface CameraAnimation {
    val name: String
    val numberOfFrames: UInt
    val startOffset: UInt
}

data class FreeCameraAnimation(
    override val name: String,
    override val numberOfFrames: UInt,
    override val startOffset: UInt,
    val freeCameraFrames: List<FreeCameraAnimationFrame>,
) : CameraAnimation

data class LookAtCameraAnimation(
    override val name: String,
    override val numberOfFrames: UInt,
    override val startOffset: UInt,
    val lookAtCameraFrames: List<LookAtCameraAnimationFrame>,
    val lookAtTargetFrames: List<LookAtTargetCameraAnimationFrame>
) : CameraAnimation
