package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readUShortPrefixedString
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class UShortPrefixedStringDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<String>
) : Deserializer<String> {

    override fun deserialize(inputStream: CountingInputStream): String {
        return inputStream.readUShortPrefixedString().also {
            postProcessor.postProcess(it, context)
        }
    }
}
