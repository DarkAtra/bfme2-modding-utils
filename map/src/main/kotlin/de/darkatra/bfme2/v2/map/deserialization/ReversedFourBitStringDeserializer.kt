package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import java.nio.charset.StandardCharsets

internal class ReversedFourBitStringDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<String>
) : Deserializer<String> {

    override fun deserialize(inputStream: CountingInputStream): String {
        return inputStream.readNBytes(4).toString(StandardCharsets.UTF_8).reversed().also {
            postProcessor.postProcess(it, context)
        }
    }
}
