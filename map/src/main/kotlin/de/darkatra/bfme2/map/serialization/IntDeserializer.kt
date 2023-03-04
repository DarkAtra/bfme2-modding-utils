package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.readInt
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
