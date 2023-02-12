package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializePostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

class UIntDeserializer(
    @Resolve(using = DeserializePostProcessorArgumentResolver::class)
    private val postProcessor: PostProcessor<UInt>
) : Deserializer<UInt> {

    override fun deserialize(inputStream: CountingInputStream, deserializationContext: DeserializationContext): UInt {
        return inputStream.readUInt().also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }
}
