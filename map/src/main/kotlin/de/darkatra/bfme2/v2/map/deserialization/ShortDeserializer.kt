package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readShort
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
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
