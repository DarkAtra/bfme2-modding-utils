package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionHolder
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import io.goodforgod.graalvm.hint.annotation.ReflectionHint
import java.io.OutputStream

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
internal class AssetListSerde<T : Any>(
    private val serializationContext: SerializationContext,
    private val entrySerde: Serde<T>,
    private val preProcessor: PreProcessor<List<T>>,
    private val postProcessor: PostProcessor<List<T>>
) : Serde<List<T>> {

    override fun calculateDataSection(data: List<T>): DataSection {
        return DataSectionHolder(
            containingData = buildList {
                data.forEach {
                    add(DataSectionLeaf.ASSET_HEADER)
                    add(entrySerde.calculateDataSection(it))
                }
            }
        )
    }

    override fun serialize(outputStream: OutputStream, data: List<T>) {

        preProcessor.preProcess(data, serializationContext).let { list ->
            list.forEach { entry ->
                MapFileWriter.writeAsset(outputStream, serializationContext, entry, entrySerde)
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
