package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.readUIntAsBoolean
import org.apache.commons.io.input.CountingInputStream

internal class UIntBooleanDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<Boolean>
) : Deserializer<Boolean> {

    override fun deserialize(inputStream: CountingInputStream): Boolean {
        return inputStream.readUIntAsBoolean().also {
            postProcessor.postProcess(it, context)
        }
    }
}
