package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readNullTerminatedString
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class StringDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<String>
) : Deserializer<String> {

    override fun deserialize(inputStream: CountingInputStream): String {
        return inputStream.readNullTerminatedString().also {
            postProcessor.postProcess(it, context)
        }
    }
}
