package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.readByte
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
