package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializePostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

class UShortDeserializer(
    @Resolve(using = DeserializePostProcessorArgumentResolver::class)
    private val postProcessor: PostProcessor<UShort>
) : Deserializer<UShort> {

    override fun deserialize(inputStream: CountingInputStream, deserializationContext: DeserializationContext): UShort {
        return inputStream.readUShort().also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }
}
