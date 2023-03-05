package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.writeUInt
import org.apache.commons.io.input.CountingInputStream
import java.io.OutputStream

internal class ColorSerde(
    private val context: SerializationContext,
    private val preProcessor: PreProcessor<Color>,
    private val postProcessor: PostProcessor<Color>
) : Serde<Color> {

    override fun calculateByteCount(data: Color): Long = 4

    override fun serialize(outputStream: OutputStream, data: Color) {
        preProcessor.preProcess(data, context).let {
            outputStream.writeUInt(
                // TODO: add exception if r, g, b or a is not fitting into exactly one byte
                (it.b.toUInt() and 0xFFu) + ((it.g.toUInt() and 0xFFu) shl 8) + ((it.r.toUInt() and 0xFFu) shl 16) + ((it.a.toUInt() and 0xFFu) shl 24)
            )
        }
    }

    override fun deserialize(inputStream: CountingInputStream): Color {
        return inputStream.readUInt().let {
            Color(
                r = ((it shr 16) and 0xFFu).toInt(),
                g = ((it shr 8) and 0xFFu).toInt(),
                b = (it and 0xFFu).toInt(),
                a = ((it shr 24) and 0xFFu).toInt()
            )
        }.also {
            postProcessor.postProcess(it, context)
        }
    }
}
