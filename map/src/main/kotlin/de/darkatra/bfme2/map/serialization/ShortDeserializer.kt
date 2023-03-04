package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.readShort
import org.apache.commons.io.input.CountingInputStream

internal class ShortDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<Short>
) : Deserializer<Short> {

    override fun deserialize(inputStream: CountingInputStream): Short {
        return inputStream.readShort().also {
            postProcessor.postProcess(it, context)
        }
    }
}
