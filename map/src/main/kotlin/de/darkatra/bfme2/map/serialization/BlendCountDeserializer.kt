package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.NoopPostProcessor
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class BlendCountDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<UInt>
) : Deserializer<UInt> {

    private val uIntDeserializer = UIntDeserializer(context, NoopPostProcessor())

    override fun deserialize(inputStream: CountingInputStream): UInt {
        // the game subtracts 1 from blendsCount and cliffBlendsCount if the deserialized value is greater than 0 for some weird reason
        return (uIntDeserializer.deserialize(inputStream) - 1u).coerceAtLeast(0u).also {
            postProcessor.postProcess(it, context)
        }
    }
}
