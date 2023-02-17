package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class ByteColorDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<Color>
) : Deserializer<Color> {

    override fun deserialize(inputStream: CountingInputStream): Color {

        val r = inputStream.readByte().toInt()
        val g = inputStream.readByte().toInt()
        val b = inputStream.readByte().toInt()
        val unusedAlpha = inputStream.readByte()
        if (unusedAlpha != 0.toByte()) {
            throw InvalidDataException("Expected unusedAlpha to be 0 using ${ByteColorDeserializer::class.simpleName}.")
        }

        return Color(r, g, b, 0).also {
            postProcessor.postProcess(it, context)
        }
    }
}
