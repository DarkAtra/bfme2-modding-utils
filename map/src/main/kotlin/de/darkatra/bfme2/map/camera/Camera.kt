package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.Vector3
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
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
