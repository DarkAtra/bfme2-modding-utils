package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import org.apache.commons.io.input.CountingInputStream
import java.io.OutputStream

internal class AssetListSerde<T>(
    private val serializationContext: SerializationContext,
    private val entrySerde: Serde<T>,
    private val preProcessor: PreProcessor<List<T>>,
    private val postProcessor: PostProcessor<List<T>>
) : Serde<List<T>> {

    override fun calculateByteCount(data: List<T>): Long {
        return data.sumOf {
            4 + 2 + 4 + entrySerde.calculateByteCount(it)
        }
    }

    override fun serialize(outputStream: OutputStream, data: List<T>) {

        preProcessor.preProcess(data, serializationContext).let { list ->
            list.forEach { entry ->
                // TODO: serialize asset header
                entrySerde.serialize(outputStream, entry)
            }
        }
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
