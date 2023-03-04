package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.readBoolean
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
