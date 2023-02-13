package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.read7BitInt
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class SevenBitIntDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<Int>
) : Deserializer<Int> {

    override fun deserialize(inputStream: CountingInputStream): Int {
        return inputStream.read7BitInt().also {
            postProcessor.postProcess(it, context)
        }
    }
}
