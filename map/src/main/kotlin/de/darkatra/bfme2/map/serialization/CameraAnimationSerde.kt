package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.map.camera.CameraAnimation
import de.darkatra.bfme2.map.camera.CameraAnimationFrame
import de.darkatra.bfme2.map.camera.CameraAnimationFrameInterpolationType
import de.darkatra.bfme2.map.camera.CameraAnimationType
import de.darkatra.bfme2.map.camera.FreeCameraAnimationFrame
import de.darkatra.bfme2.map.camera.LookAtCameraAnimationFrame
import de.darkatra.bfme2.map.camera.LookAtCameraLookAtAnimationFrame
import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionHolder
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import de.darkatra.bfme2.writeUInt
import de.darkatra.bfme2.writeUShortPrefixedString
import java.io.OutputStream

internal class CameraAnimationSerde(
    serdeFactory: SerdeFactory,
    private val serializationContext: SerializationContext,
    private val preProcessor: PreProcessor<CameraAnimation>,
    private val postProcessor: PostProcessor<CameraAnimation>
) : Serde<CameraAnimation> {

    private val cameraAnimationTypeSerde = serdeFactory.getSerde(CameraAnimationType::class)
    private val cameraAnimationFrameInterpolationTypeSerde = serdeFactory.getSerde(CameraAnimationFrameInterpolationType::class)

    private val freeCameraAnimationFrameSerde = serdeFactory.getSerde(FreeCameraAnimationFrame::class)
    private val lookAtCameraAnimationFrameSerde = serdeFactory.getSerde(LookAtCameraAnimationFrame::class)
    private val lookAtCameraLookAtAnimationFrameSerde = serdeFactory.getSerde(LookAtCameraLookAtAnimationFrame::class)

    override fun calculateDataSection(data: CameraAnimation): DataSection {

        return DataSectionHolder(
            containingData = listOf(
                cameraAnimationTypeSerde.calculateDataSection(data.animationType),

                DataSectionLeaf(2L + data.name.length),
                DataSectionLeaf.INT,
                DataSectionLeaf.INT,

                DataSectionLeaf.INT
            ) + data.cameraFrames.map { cameraAnimationFrame ->
                when (cameraAnimationFrame) {
                    is FreeCameraAnimationFrame -> freeCameraAnimationFrameSerde.calculateDataSection(cameraAnimationFrame)
                    is LookAtCameraAnimationFrame -> lookAtCameraAnimationFrameSerde.calculateDataSection(cameraAnimationFrame)
                    is LookAtCameraLookAtAnimationFrame -> lookAtCameraLookAtAnimationFrameSerde.calculateDataSection(cameraAnimationFrame)
                }
            }
        )
    }

    override fun serialize(outputStream: OutputStream, data: CameraAnimation) {

        preProcessor.preProcess(data, serializationContext).let { cameraAnimation ->

            cameraAnimationTypeSerde.serialize(outputStream, cameraAnimation.animationType)

            outputStream.writeUShortPrefixedString(cameraAnimation.name)
            outputStream.writeUInt(cameraAnimation.numberOfFrames)
            outputStream.writeUInt(cameraAnimation.startOffset)

            writeCameraAnimationCameraFrames(outputStream, cameraAnimation.cameraFrames)
        }
    }

    private fun writeCameraAnimationCameraFrames(outputStream: OutputStream, cameraAnimationFrames: List<CameraAnimationFrame>) {

        outputStream.writeUInt(cameraAnimationFrames.size.toUInt())

        cameraAnimationFrames.forEach { cameraAnimationFrame ->

            outputStream.writeUInt(cameraAnimationFrame.frameIndex)
            cameraAnimationFrameInterpolationTypeSerde.serialize(outputStream, cameraAnimationFrame.interpolationType)

            // TODO: maybe validate that each LookAtCameraAnimationFrame is followed by an LookAtCameraLookAtAnimationFrame
            when (cameraAnimationFrame) {
                is FreeCameraAnimationFrame -> freeCameraAnimationFrameSerde.serialize(outputStream, cameraAnimationFrame)
                is LookAtCameraAnimationFrame -> lookAtCameraAnimationFrameSerde.serialize(outputStream, cameraAnimationFrame)
                is LookAtCameraLookAtAnimationFrame -> lookAtCameraLookAtAnimationFrameSerde.serialize(outputStream, cameraAnimationFrame)
            }
        }
    }

    override fun deserialize(inputStream: CountingInputStream): CameraAnimation {

        val animationType = cameraAnimationTypeSerde.deserialize(inputStream)

        val name = inputStream.readUShortPrefixedString()
        val numberOfFrames = inputStream.readUInt()
        val startOffset = inputStream.readUInt()

        val cameraAnimationFrames: List<CameraAnimationFrame> = when (animationType) {
            CameraAnimationType.FREE -> readCameraAnimationCameraFrames(inputStream, freeCameraAnimationFrameSerde::deserialize)
            CameraAnimationType.LOOK -> readCameraAnimationCameraFrames(inputStream, lookAtCameraAnimationFrameSerde::deserialize) +
                readCameraAnimationCameraFrames(inputStream, lookAtCameraLookAtAnimationFrameSerde::deserialize)
        }

        return CameraAnimation(
            animationType = animationType,
            name = name,
            numberOfFrames = numberOfFrames,
            startOffset = startOffset,
            cameraFrames = cameraAnimationFrames
        ).also {
            postProcessor.postProcess(it, serializationContext)
        }
    }

    private fun <T : CameraAnimationFrame> readCameraAnimationCameraFrames(inputStream: CountingInputStream, frameReader: (CountingInputStream) -> T): List<T> {

        val numberOfFrames = inputStream.readUInt()
        val animationFrames = mutableListOf<T>()
        for (i in 0u until numberOfFrames step 1) {
            animationFrames.add(
                frameReader(inputStream)
            )
        }

        return animationFrames
    }
}
