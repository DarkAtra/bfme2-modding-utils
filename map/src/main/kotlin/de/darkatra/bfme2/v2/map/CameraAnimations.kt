package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.Vector4
import de.darkatra.bfme2.v2.map.deserialization.ConditionalDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.ReversedFourBitStringDeserializer

@Asset(name = "CameraAnimationList", version = 3u)
data class CameraAnimations(
    val animations: List<CameraAnimation>
) {

    data class CameraAnimation(
        val animationType: CameraAnimationType,
        val name: String,
        val numberOfFrames: UInt,
        val startOffset: UInt,
        val cameraFrames: List<CameraAnimationFrame>,
        val lookAtFrames: List<CameraAnimationFrame>
    ) {

        enum class CameraAnimationType(
            internal val rawName: @Deserialize(using = ReversedFourBitStringDeserializer::class) String
        ) {
            FREE("EERF"),
            LOOK("KOOL")
        }

        enum class CameraAnimationFrameInterpolationType(
            internal val rawName: @Deserialize(using = ReversedFourBitStringDeserializer::class) String
        ) {
            CATM("MTAC"),
            LINE("ENIL")
        }

        @Deserialize(using = ConditionalDeserializer::class)
        @ConditionalDeserializer.Properties(assetTypes = [FreeCameraAnimationCameraFrame::class, LookAtCameraAnimationCameraFrame::class, LookAtCameraAnimationLookAtFrame::class])
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
    }
}
