package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.readUShort
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
