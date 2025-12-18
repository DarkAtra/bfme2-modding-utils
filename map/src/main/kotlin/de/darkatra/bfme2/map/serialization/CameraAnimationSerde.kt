package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.map.camera.CameraAnimation
import de.darkatra.bfme2.map.camera.CameraAnimationType
import de.darkatra.bfme2.map.camera.FreeCameraAnimation
import de.darkatra.bfme2.map.camera.LookAtCameraAnimation
import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionHolder
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import io.goodforgod.graalvm.hint.annotation.ReflectionHint
import java.io.OutputStream

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
internal class CameraAnimationSerde(
    serdeFactory: SerdeFactory,
    private val serializationContext: SerializationContext,
    private val preProcessor: PreProcessor<CameraAnimation>,
    private val postProcessor: PostProcessor<CameraAnimation>
) : Serde<CameraAnimation> {

    private val cameraAnimationTypeSerde = serdeFactory.getSerde(CameraAnimationType::class)
    private val freeCameraAnimationSerde = serdeFactory.getSerde(FreeCameraAnimation::class)
    private val lookAtCameraAnimationSerde = serdeFactory.getSerde(LookAtCameraAnimation::class)

    override fun calculateDataSection(data: CameraAnimation): DataSection {

        return when (data) {
            is FreeCameraAnimation -> DataSectionHolder(
                containingData = listOf(
                    cameraAnimationTypeSerde.calculateDataSection(CameraAnimationType.FREE),
                    freeCameraAnimationSerde.calculateDataSection(data)
                )
            )

            is LookAtCameraAnimation -> DataSectionHolder(
                containingData = listOf(
                    cameraAnimationTypeSerde.calculateDataSection(CameraAnimationType.LOOK),
                    lookAtCameraAnimationSerde.calculateDataSection(data)
                )
            )
        }
    }

    override fun serialize(outputStream: OutputStream, data: CameraAnimation) {

        preProcessor.preProcess(data, serializationContext).let { cameraAnimation ->
            when (cameraAnimation) {
                is FreeCameraAnimation -> {
                    cameraAnimationTypeSerde.serialize(outputStream, CameraAnimationType.FREE)
                    freeCameraAnimationSerde.serialize(outputStream, cameraAnimation)
                }

                is LookAtCameraAnimation -> {
                    cameraAnimationTypeSerde.serialize(outputStream, CameraAnimationType.LOOK)
                    lookAtCameraAnimationSerde.serialize(outputStream, cameraAnimation)
                }
            }
        }
    }

    override fun deserialize(inputStream: CountingInputStream): CameraAnimation {

        val animationType = cameraAnimationTypeSerde.deserialize(inputStream)
        return when (animationType) {
            CameraAnimationType.FREE -> freeCameraAnimationSerde.deserialize(inputStream)
            CameraAnimationType.LOOK -> lookAtCameraAnimationSerde.deserialize(inputStream)
        }.also {
            postProcessor.postProcess(it, serializationContext)
        }
    }
}
