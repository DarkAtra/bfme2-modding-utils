package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

@UseDeserializerProperties(ListDeserializer.ListDeserializerProperties::class)
internal class ListDeserializer<T>(
    private val context: DeserializationContext,
    private val entryDeserializer: Deserializer<T>,
    private val postProcessor: PostProcessor<List<T>>,

    private val sizeType: SizeType
) : Deserializer<List<T>> {

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE)
    @DeserializerProperties
    @Suppress("unused") // properties are used via AnnotationParameterArgumentResolver
    annotation class ListDeserializerProperties(
        val sizeType: SizeType = SizeType.UINT
    )

    enum class SizeType {
        UINT,
        USHORT
    }

    override fun deserialize(inputStream: CountingInputStream): List<T> {

        val numberOfListEntries = when (sizeType) {
            SizeType.UINT -> inputStream.readUInt()
            SizeType.USHORT -> inputStream.readUShort().toUInt()
        }

        val list = mutableListOf<T>()
        for (i in 0u until numberOfListEntries step 1) {
            list.add(entryDeserializer.deserialize(inputStream))
        }

        return list.also {
            postProcessor.postProcess(it, context)
        }
    }
}
