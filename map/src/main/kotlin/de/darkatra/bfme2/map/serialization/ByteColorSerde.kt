package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.writeByte
import org.apache.commons.io.input.CountingInputStream
import java.io.OutputStream

internal class ByteColorSerde(
    private val context: SerializationContext,
    private val preProcessor: PreProcessor<Color>,
    private val postProcessor: PostProcessor<Color>
) : Serde<Color> {

    override fun calculateByteCount(data: Color): Long = 4

    override fun serialize(outputStream: OutputStream, data: Color) {
        preProcessor.preProcess(data, context).let {
            outputStream.writeByte(it.r.toByte())
            outputStream.writeByte(it.g.toByte())
            outputStream.writeByte(it.b.toByte())
            outputStream.writeByte(0)
        }
    }

    override fun deserialize(inputStream: CountingInputStream): Color {

        val r = inputStream.readByte().toInt()
        val g = inputStream.readByte().toInt()
        val b = inputStream.readByte().toInt()
        val unusedAlpha = inputStream.readByte()
        if (unusedAlpha != 0.toByte()) {
            throw InvalidDataException("Expected unusedAlpha to be 0 using ${ByteColorSerde::class.simpleName}.")
        }

        return Color(r, g, b, 0).also {
            postProcessor.postProcess(it, context)
        }
    }
}
