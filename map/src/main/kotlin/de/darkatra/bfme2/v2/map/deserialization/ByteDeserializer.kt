package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class ByteDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<Byte>
) : Deserializer<Byte> {

    override fun deserialize(inputStream: CountingInputStream): Byte {
        return inputStream.readByte().also {
            postProcessor.postProcess(it, context)
        }
    }
}
