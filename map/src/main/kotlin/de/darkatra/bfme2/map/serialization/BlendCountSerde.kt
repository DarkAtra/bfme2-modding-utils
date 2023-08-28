package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.postprocessing.NoopPostProcessor
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.NoopPreProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import java.io.OutputStream

internal class BlendCountSerde(
    private val context: SerializationContext,
    private val preProcessor: PreProcessor<UInt>,
    private val postProcessor: PostProcessor<UInt>
) : Serde<UInt> {

    private val uIntSerde = UIntSerde(context, NoopPreProcessor(), NoopPostProcessor())

    override fun calculateDataSection(data: UInt): DataSection {
        return uIntSerde.calculateDataSection(data)
    }

    override fun serialize(outputStream: OutputStream, data: UInt) {
        uIntSerde.serialize(
            outputStream,
            // the game adds 1 to blendsCount and cliffBlendsCount if the value is greater than 0 for some weird reason
            preProcessor.preProcess(if (data > 0u) data + 1u else data, context)
        )
    }

    override fun deserialize(inputStream: CountingInputStream): UInt {
        val value = uIntSerde.deserialize(inputStream)
        return when {
            value > 0u -> value - 1u
            else -> value
        }.also {
            postProcessor.postProcess(it, context)
        }
    }
}
