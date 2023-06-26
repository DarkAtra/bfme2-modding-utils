package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.Color
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.writeUInt
import java.io.OutputStream

internal class ColorSerde(
    private val context: SerializationContext,
    private val preProcessor: PreProcessor<Color>,
    private val postProcessor: PostProcessor<Color>
) : Serde<Color> {

    override fun calculateByteCount(data: Color): Long = 4

    override fun serialize(outputStream: OutputStream, data: Color) {
        preProcessor.preProcess(data, context).let {
            outputStream.writeUInt(it.rgba)
        }
    }

    override fun deserialize(inputStream: CountingInputStream): Color {
        return Color(
            rgba = inputStream.readUInt()
        ).also {
            postProcessor.postProcess(it, context)
        }
    }
}
