package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.read7BitIntPrefixedString
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializePostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

class SevenBitIntPrefixedStringDeserializer(
    @Resolve(using = DeserializePostProcessorArgumentResolver::class)
    private val postProcessor: PostProcessor<String>
) : Deserializer<String> {

    override fun deserialize(inputStream: CountingInputStream, deserializationContext: DeserializationContext): String {
        return inputStream.read7BitIntPrefixedString().also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }
}
