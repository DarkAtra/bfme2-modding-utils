package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import java.io.OutputStream

internal class AssetListSerde<T>(
    private val serializationContext: SerializationContext,
    private val entrySerde: Serde<T>,
    private val postProcessor: PostProcessor<List<T>>
) : Serde<List<T>> {

    override fun serialize(outputStream: OutputStream, data: List<T>) {
        TODO("Not yet implemented")
    }

    override fun deserialize(inputStream: CountingInputStream): List<T> {

        val list = mutableListOf<T>()

        MapFileReader.readAssets(inputStream, serializationContext) {
            list.add(entrySerde.deserialize(inputStream))
        }

        return list.also {
            postProcessor.postProcess(list, serializationContext)
        }
    }
}
