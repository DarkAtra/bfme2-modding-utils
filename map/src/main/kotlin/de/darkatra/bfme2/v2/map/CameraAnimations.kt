package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.Vector4
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.FourBitStringDeserializer

@Asset(name = "CameraAnimationList", version = 3u)
data class CameraAnimations(
    val animations: List<CameraAnimation>
) {

    data class CameraAnimation(
        val animationType: CameraAnimationType,
        val name: String,
        val numberOfFrames: UInt,
        val startOffset: UInt,
        val cameraFrames: List<FreeCameraAnimationCameraFrame>
    ) {

        enum class CameraAnimationType(
            internal val rawName: @Deserialize(using = FourBitStringDeserializer::class) String
        ) {
            FREE("EERF"),
            LOOK("KOOL")
        }

        enum class CameraAnimationFrameInterpolationType(
            internal val rawName: @Deserialize(using = FourBitStringDeserializer::class) String
        ) {
            CATM("MTAC"),
            LINE("ENIL")
        }

        data class FreeCameraAnimationCameraFrame(
            val frameIndex: UInt,
            val interpolationType: CameraAnimationFrameInterpolationType,
            val position: Vector3,
            val rotation: Vector4,
            val fieldOfView: Float
        )
    }
}
