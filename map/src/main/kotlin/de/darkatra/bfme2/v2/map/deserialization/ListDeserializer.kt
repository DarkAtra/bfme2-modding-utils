package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.ArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializationContextResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializerArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.PostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.full.findAnnotation

internal class ListDeserializer<T>(
    @Resolve(using = DeserializerArgumentResolver::class)
    private val entryDeserializer: Deserializer<T>,
    @Resolve(using = SizeTypeArgumentResolver::class)
    private val sizeType: SizeType,
    @Resolve(using = DeserializationContextResolver::class)
    private val context: DeserializationContext,
    @Resolve(using = PostProcessorArgumentResolver::class)
    private val postProcessor: PostProcessor<List<T>>
) : Deserializer<List<T>> {

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE)
    annotation class ListDeserializerProperties(
        val sizeType: SizeType
    )

    // TODO: think about a meta annotation on ListDeserializerProperties and a generic AnnotationParameterArgumentResolver
    class SizeTypeArgumentResolver : ArgumentResolver<SizeType> {
        override fun resolve(currentElement: ProcessableElement): SizeType {
            val deserializerProperties = currentElement.getType().findAnnotation<ListDeserializerProperties>()
            return deserializerProperties
                ?.sizeType
                ?: SizeType.UINT
        }
    }

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
