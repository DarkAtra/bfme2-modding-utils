package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.Vector4
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.CameraAnimation
import de.darkatra.bfme2.map.CameraAnimationFrame
import de.darkatra.bfme2.map.CameraAnimationFrameInterpolationType
import de.darkatra.bfme2.map.CameraAnimationType
import de.darkatra.bfme2.map.FreeCameraAnimationCameraFrame
import de.darkatra.bfme2.map.LookAtCameraAnimationCameraFrame
import de.darkatra.bfme2.map.LookAtCameraAnimationLookAtFrame
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream
import java.nio.charset.StandardCharsets

class CamerasAnimationsReader : AssetReader {

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

        MapFileReader.readAsset(reader, context, AssetName.POLYGON_TRIGGERS.assetName) {

            val numberOfAnimations = reader.readUInt()

            val cameraAnimations = mutableListOf<CameraAnimation>()
            for (i in 0u until numberOfAnimations step 1) {
                cameraAnimations.add(
                    readCameraAnimations(reader)
                )
            }

            builder.cameraAnimations(cameraAnimations)
        }
    }

    private fun readCameraAnimations(reader: CountingInputStream): CameraAnimation {

        // chars are in reversed order
        val animationType = CameraAnimationType.ofName(reader.readNBytes(4).toString(StandardCharsets.UTF_8).reversed())

        val name = reader.readUShortPrefixedString()
        val numberOfFrames = reader.readUInt()
        val startOffset = reader.readUInt()

        val cameraFrames = when (animationType) {
            CameraAnimationType.FREE -> readCameraAnimationCameraFrames(reader, this::readFreeCameraAnimationCameraFrame)
            CameraAnimationType.LOOK -> readCameraAnimationCameraFrames(reader, this::readLookAtCameraAnimationCameraFrame)
        }

        val lookAtFrames = when (animationType) {
            CameraAnimationType.FREE -> null
            CameraAnimationType.LOOK -> readCameraAnimationCameraFrames(reader, this::readLookAtCameraAnimationLookAtFrame)
        }

        return CameraAnimation(
            animationType = animationType,
            name = name,
            numberOfFrames = numberOfFrames,
            startOffset = startOffset,
            cameraFrames = cameraFrames,
            lookAtFrames = lookAtFrames
        )
    }

    private fun readCameraAnimationCameraFrames(
        reader: CountingInputStream,
        frameParser: (CountingInputStream, UInt, CameraAnimationFrameInterpolationType) -> CameraAnimationFrame
    ): List<CameraAnimationFrame> {

        val numberOfFrames = reader.readUInt()
        val animationFrames = mutableListOf<CameraAnimationFrame>()
        for (i in 0u until numberOfFrames step 1) {

            val frameIndex = reader.readUInt()
            val interpolationType = CameraAnimationFrameInterpolationType.ofName(
                reader.readNBytes(4).toString(StandardCharsets.UTF_8).reversed()
            )

            animationFrames.add(
                frameParser(reader, frameIndex, interpolationType)
            )
        }

        return animationFrames
    }

    private fun readFreeCameraAnimationCameraFrame(
        reader: CountingInputStream,
        frameIndex: UInt,
        interpolationType: CameraAnimationFrameInterpolationType
    ): FreeCameraAnimationCameraFrame {

        return FreeCameraAnimationCameraFrame(
            frameIndex = frameIndex,
            interpolationType = interpolationType,
            position = Vector3(
                x = reader.readFloat(),
                y = reader.readFloat(),
                z = reader.readFloat()
            ),
            rotation = Vector4(
                x = reader.readFloat(),
                y = reader.readFloat(),
                z = reader.readFloat(),
                w = reader.readFloat()
            ),
            fieldOfView = reader.readFloat()
        )
    }

    private fun readLookAtCameraAnimationCameraFrame(
        reader: CountingInputStream,
        frameIndex: UInt,
        interpolationType: CameraAnimationFrameInterpolationType
    ): LookAtCameraAnimationCameraFrame {

        return LookAtCameraAnimationCameraFrame(
            frameIndex = frameIndex,
            interpolationType = interpolationType,
            position = Vector3(
                x = reader.readFloat(),
                y = reader.readFloat(),
                z = reader.readFloat()
            ),
            roll = reader.readFloat(),
            fieldOfView = reader.readFloat()
        )
    }

    private fun readLookAtCameraAnimationLookAtFrame(
        reader: CountingInputStream,
        frameIndex: UInt,
        interpolationType: CameraAnimationFrameInterpolationType
    ): LookAtCameraAnimationLookAtFrame {

        return LookAtCameraAnimationLookAtFrame(
            frameIndex = frameIndex,
            interpolationType = interpolationType,
            lookAt = Vector3(
                x = reader.readFloat(),
                y = reader.readFloat(),
                z = reader.readFloat()
            )
        )
    }
}
