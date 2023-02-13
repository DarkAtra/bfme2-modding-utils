package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.ArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializePostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.ListEntryArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.full.findAnnotation

class ListDeserializer<T>(
    @Resolve(using = ListEntryArgumentResolver::class)
    private val entryDeserializer: Deserializer<T>,
    @Resolve(using = SizeTypeArgumentResolver::class)
    private val sizeType: SizeType,
    @Resolve(using = DeserializePostProcessorArgumentResolver::class)
    private val postProcessor: PostProcessor<List<T>>
) : Deserializer<List<T>> {

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE)
    annotation class ListDeserializerProperties(
        val sizeType: SizeType
    )

    class SizeTypeArgumentResolver : ArgumentResolver<SizeType> {
        override fun resolve(deserializationContext: DeserializationContext): SizeType {
            val deserializerProperties = deserializationContext.getCurrentElement().getType().findAnnotation<ListDeserializerProperties>()
            return deserializerProperties
                ?.sizeType
                ?: SizeType.UINT
        }
    }

    enum class SizeType {
        UINT,
        USHORT
    }

    override fun deserialize(inputStream: CountingInputStream, deserializationContext: DeserializationContext): List<T> {

        val numberOfListEntries = when (sizeType) {
            SizeType.UINT -> inputStream.readUInt()
            SizeType.USHORT -> inputStream.readUShort().toUInt()
        }

        val list = mutableListOf<T>()
        for (i in 0u until numberOfListEntries step 1) {
            list.add(entryDeserializer.deserialize(inputStream, deserializationContext))
        }

        return list.also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }
}
