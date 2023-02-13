package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.read7BitIntPrefixedString
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class SevenBitIntPrefixedStringDeserializer(
    private val context: DeserializationContext,
    private val postProcessor: PostProcessor<String>
) : Deserializer<String> {

    override fun deserialize(inputStream: CountingInputStream): String {
        return inputStream.read7BitIntPrefixedString().also {
            postProcessor.postProcess(it, context)
        }
    }
}
