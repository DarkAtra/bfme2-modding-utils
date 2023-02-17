package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class ColorDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<Color>
) : Deserializer<Color> {

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
