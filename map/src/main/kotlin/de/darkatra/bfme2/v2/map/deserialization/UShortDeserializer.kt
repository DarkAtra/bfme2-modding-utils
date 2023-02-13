package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializationContextResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.PostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class UShortDeserializer(
    @Resolve(using = DeserializationContextResolver::class)
    private val context: DeserializationContext,
    @Resolve(using = PostProcessorArgumentResolver::class)
    private val postProcessor: PostProcessor<UShort>
) : Deserializer<UShort> {

    override fun deserialize(inputStream: CountingInputStream): UShort {
        return inputStream.readUShort().also {
            postProcessor.postProcess(it, context)
        }
    }
}
