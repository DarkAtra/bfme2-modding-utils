package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializePostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.ListEntryArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

class ListDeserializer<T>(
    @Resolve(using = ListEntryArgumentResolver::class)
    private val entryDeserializer: Deserializer<T>,
    @Resolve(using = DeserializePostProcessorArgumentResolver::class)
    private val postProcessor: PostProcessor<List<T>>
) : Deserializer<List<T>> {

    override fun deserialize(inputStream: CountingInputStream, deserializationContext: DeserializationContext): List<T> {

        val numberOfListEntries = inputStream.readUInt()

        val list = mutableListOf<T>()
        for (i in 0u until numberOfListEntries step 1) {
            list.add(entryDeserializer.deserialize(inputStream, deserializationContext))
        }

        return list.also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }
}
