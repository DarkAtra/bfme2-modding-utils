package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.NoopPostProcessor
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.NoopPreProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import org.apache.commons.io.input.CountingInputStream
import java.io.OutputStream

internal class BlendCountSerde(
    private val context: SerializationContext,
    private val preProcessor: PreProcessor<UInt>,
    private val postProcessor: PostProcessor<UInt>
) : Serde<UInt> {

    private val uIntSerde = UIntSerde(context, NoopPreProcessor(), NoopPostProcessor())

    override fun calculateByteCount(data: UInt): Long = uIntSerde.calculateByteCount(data)

    override fun serialize(outputStream: OutputStream, data: UInt) {
        uIntSerde.serialize(
            outputStream,
            // the game adds 1 to blendsCount and cliffBlendsCount if the value is greater than 0 for some weird reason
            preProcessor.preProcess(if (data > 0u) data + 1u else data, context)
        )
    }

    override fun deserialize(inputStream: CountingInputStream): UInt {
        // the game subtracts 1 from blendsCount and cliffBlendsCount if the deserialized value is greater than 0 for some weird reason
        return (uIntSerde.deserialize(inputStream) - 1u).coerceAtLeast(0u).also {
            postProcessor.postProcess(it, context)
        }
    }
}
