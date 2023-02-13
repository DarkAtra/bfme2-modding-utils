package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class UShortDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<UShort>
) : Deserializer<UShort> {

    override fun deserialize(inputStream: CountingInputStream): UShort {
        return inputStream.readUShort().also {
            postProcessor.postProcess(it, context)
        }
    }
}
