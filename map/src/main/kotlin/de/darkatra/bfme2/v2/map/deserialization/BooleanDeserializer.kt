package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializationContextResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.PostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class BooleanDeserializer(
    @Resolve(using = DeserializationContextResolver::class)
    private val context: DeserializationContext,
    @Resolve(using = PostProcessorArgumentResolver::class)
    private val postProcessor: PostProcessor<Boolean>
) : Deserializer<Boolean> {

    override fun deserialize(inputStream: CountingInputStream): Boolean {
        return inputStream.readBoolean().also {
            postProcessor.postProcess(it, context)
        }
    }
}
