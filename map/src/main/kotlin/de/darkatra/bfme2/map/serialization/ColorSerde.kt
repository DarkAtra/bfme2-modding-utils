package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.Color
import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.postprocessing.NoopPostProcessor
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.NoopPreProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import java.io.OutputStream

internal class ColorSerde(
    private val context: SerializationContext,
    private val preProcessor: PreProcessor<Color>,
    private val postProcessor: PostProcessor<Color>
) : Serde<Color> {

    private val uIntSerde = UIntSerde(context, NoopPreProcessor(), NoopPostProcessor())

    override fun collectDataSections(data: Color): DataSection = uIntSerde.collectDataSections(data.rgba)

    override fun serialize(outputStream: OutputStream, data: Color) {
        preProcessor.preProcess(data, context).let {
            uIntSerde.serialize(outputStream, it.rgba)
        }
    }

    override fun deserialize(inputStream: CountingInputStream): Color {
        return Color(
            rgba = uIntSerde.deserialize(inputStream)
        ).also {
            postProcessor.postProcess(it, context)
        }
    }
}
