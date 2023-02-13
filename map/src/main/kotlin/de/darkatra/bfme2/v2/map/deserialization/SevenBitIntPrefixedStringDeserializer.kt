package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.read7BitIntPrefixedString
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializationContextResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.PostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class SevenBitIntPrefixedStringDeserializer(
    @Resolve(using = DeserializationContextResolver::class)
    private val context: DeserializationContext,
    @Resolve(using = PostProcessorArgumentResolver::class)
    private val postProcessor: PostProcessor<String>
) : Deserializer<String> {

    override fun deserialize(inputStream: CountingInputStream): String {
        return inputStream.read7BitIntPrefixedString().also {
            postProcessor.postProcess(it, context)
        }
    }
}
