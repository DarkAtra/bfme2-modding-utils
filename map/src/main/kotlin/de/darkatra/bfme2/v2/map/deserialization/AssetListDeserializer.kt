package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class AssetListDeserializer<T>(
    private val deserializationContext: DeserializationContext,
    private val entryDeserializer: Deserializer<T>,
    private val postProcessor: PostProcessor<List<T>>
) : Deserializer<List<T>> {

    override fun deserialize(inputStream: CountingInputStream): List<T> {

        val list = mutableListOf<T>()

        MapFileReader.readAssets(inputStream, deserializationContext) {
            list.add(entryDeserializer.deserialize(inputStream))
        }

        return list.also {
            postProcessor.postProcess(list, deserializationContext)
        }
    }
}
