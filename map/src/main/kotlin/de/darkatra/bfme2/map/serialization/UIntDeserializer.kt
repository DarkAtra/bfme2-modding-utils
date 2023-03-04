package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.readUInt
import org.apache.commons.io.input.CountingInputStream

internal class UIntDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<UInt>
) : Deserializer<UInt> {

    override fun deserialize(inputStream: CountingInputStream): UInt {
        return inputStream.readUInt().also {
            postProcessor.postProcess(it, context)
        }
    }
}
