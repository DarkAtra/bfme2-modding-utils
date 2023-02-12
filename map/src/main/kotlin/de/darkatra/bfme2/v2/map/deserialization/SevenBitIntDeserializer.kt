package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.read7BitInt
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializePostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

class SevenBitIntDeserializer(
    @Resolve(using = DeserializePostProcessorArgumentResolver::class)
    private val postProcessor: PostProcessor<Int>
) : Deserializer<Int> {

    override fun deserialize(inputStream: CountingInputStream, deserializationContext: DeserializationContext): Int {
        return inputStream.read7BitInt().also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }
}
