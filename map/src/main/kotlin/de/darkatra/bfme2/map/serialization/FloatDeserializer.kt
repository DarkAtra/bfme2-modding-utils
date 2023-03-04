package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.readFloat
import org.apache.commons.io.input.CountingInputStream

internal class FloatDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<Float>
) : Deserializer<Float> {

    override fun deserialize(inputStream: CountingInputStream): Float {
        return inputStream.readFloat().also {
            postProcessor.postProcess(it, context)
        }
    }
}
