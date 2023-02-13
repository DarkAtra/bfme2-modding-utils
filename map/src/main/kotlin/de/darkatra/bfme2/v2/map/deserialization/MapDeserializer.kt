package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.ArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializationContextResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializersArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.PostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.full.findAnnotation

internal class MapDeserializer<K, V>(
    @Resolve(using = DeserializationContextResolver::class)
    private val context: DeserializationContext,
    @Resolve(using = DeserializersArgumentResolver::class)
    private val deserializers: List<Deserializer<*>>,
    @Resolve(using = DeserializationOrderArgumentResolver::class)
    private val order: DeserializationOrder,
    @Resolve(using = PostProcessorArgumentResolver::class)
    private val postProcessor: PostProcessor<Map<K, V>>
) : Deserializer<Map<K, V>> {

    private val keyDeserializer: Deserializer<K>
    private val valueDeserializer: Deserializer<V>

    init {
        if (deserializers.size != 2) {
            error("Expected exactly two deserializer but found: $deserializers")
        }

        @Suppress("UNCHECKED_CAST")
        keyDeserializer = deserializers[0] as Deserializer<K>
        @Suppress("UNCHECKED_CAST")
        valueDeserializer = deserializers[1] as Deserializer<V>
    }

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE)
    annotation class MapDeserializerProperties(
        val deserializationOrder: DeserializationOrder
    )

    // TODO: think about a meta annotation on MapDeserializerProperties and a generic AnnotationParameterArgumentResolver
    class DeserializationOrderArgumentResolver : ArgumentResolver<DeserializationOrder> {
        override fun resolve(currentElement: ProcessableElement): DeserializationOrder {
            val deserializerProperties = currentElement.getType().findAnnotation<MapDeserializerProperties>()
            return deserializerProperties
                ?.deserializationOrder
                ?: DeserializationOrder.KEY_FIRST
        }
    }

    enum class DeserializationOrder {
        KEY_FIRST,
        VALUE_FIRST
    }

    override fun deserialize(inputStream: CountingInputStream): Map<K, V> {

        val numberOfMapEntries = inputStream.readUInt()

        val map = mutableMapOf<K, V>()
        for (i in 0u until numberOfMapEntries step 1) {
            val (key, value) = getKeyAndValue(inputStream)
            map[key] = value
        }

        return map.also {
            postProcessor.postProcess(it, context)
        }
    }

    private fun getKeyAndValue(inputStream: CountingInputStream): Pair<K, V> {
        return when (order) {
            DeserializationOrder.KEY_FIRST -> Pair(
                first = keyDeserializer.deserialize(inputStream),
                second = valueDeserializer.deserialize(inputStream)
            )

            DeserializationOrder.VALUE_FIRST -> Pair(
                second = valueDeserializer.deserialize(inputStream),
                first = keyDeserializer.deserialize(inputStream)
            )
        }
    }
}
