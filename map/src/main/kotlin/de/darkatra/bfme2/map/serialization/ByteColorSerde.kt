package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.Color
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.writeByte
import java.io.OutputStream

internal class ByteColorSerde(
    private val context: SerializationContext,
    private val preProcessor: PreProcessor<Color>,
    private val postProcessor: PostProcessor<Color>
) : Serde<Color> {

    override fun calculateByteCount(data: Color): Long = 4

    override fun serialize(outputStream: OutputStream, data: Color) {
        preProcessor.preProcess(data, context).let {
            outputStream.writeByte(it.red.toByte())
            outputStream.writeByte(it.green.toByte())
            outputStream.writeByte(it.blue.toByte())
            outputStream.writeByte(0)
        }
    }

    override fun deserialize(inputStream: CountingInputStream): Color {

        val r = inputStream.readByte().toUInt()
        val g = inputStream.readByte().toUInt()
        val b = inputStream.readByte().toUInt()

        val unusedAlpha = inputStream.readByte()
        if (unusedAlpha != 0.toByte()) {
            throw InvalidDataException("Expected unusedAlpha to be 0 using ${ByteColorSerde::class.simpleName}.")
        }

        return Color(
            red = r,
            green = g,
            blue = b
        ).also {
            postProcessor.postProcess(it, context)
        }
    }
}
