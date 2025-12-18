package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.map.serialization.CameraAnimationSerde
import de.darkatra.bfme2.map.serialization.Serialize
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@Serialize(using = CameraAnimationSerde::class)
sealed interface CameraAnimation {
    val name: String
    val numberOfFrames: UInt
    val startOffset: UInt
}

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class FreeCameraAnimation(
    override val name: String,
    override val numberOfFrames: UInt,
    override val startOffset: UInt,
    val freeCameraFrames: List<FreeCameraAnimationFrame>,
) : CameraAnimation

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class LookAtCameraAnimation(
    override val name: String,
    override val numberOfFrames: UInt,
    override val startOffset: UInt,
    val lookAtCameraFrames: List<LookAtCameraAnimationFrame>,
    val lookAtTargetFrames: List<LookAtTargetCameraAnimationFrame>
) : CameraAnimation
