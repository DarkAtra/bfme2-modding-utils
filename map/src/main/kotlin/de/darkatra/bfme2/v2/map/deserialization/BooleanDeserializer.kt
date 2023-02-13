package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class BooleanDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<Boolean>
) : Deserializer<Boolean> {

    override fun deserialize(inputStream: CountingInputStream): Boolean {
        return inputStream.readBoolean().also {
            postProcessor.postProcess(it, context)
        }
    }
}
