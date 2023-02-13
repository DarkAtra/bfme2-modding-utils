package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class IntDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<Int>
) : Deserializer<Int> {

    override fun deserialize(inputStream: CountingInputStream): Int {
        return inputStream.readInt().also {
            postProcessor.postProcess(it, context)
        }
    }
}
