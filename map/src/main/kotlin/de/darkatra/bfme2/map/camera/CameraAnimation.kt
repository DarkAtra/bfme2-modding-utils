package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.map.serialization.CameraAnimationSerde
import de.darkatra.bfme2.map.serialization.Serialize

@Serialize(using = CameraAnimationSerde::class)
data class CameraAnimation(
    val animationType: CameraAnimationType,
    val name: String,
    val numberOfFrames: UInt,
    val startOffset: UInt,
    val cameraFrames: List<CameraAnimationFrame>
)
