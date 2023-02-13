package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
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
