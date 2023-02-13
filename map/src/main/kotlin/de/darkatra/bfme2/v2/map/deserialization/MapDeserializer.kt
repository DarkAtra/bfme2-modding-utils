package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.ArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializePostProcessorArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.MapKeyArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.MapValueArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.full.findAnnotation

class MapDeserializer<K, V>(
    @Resolve(using = MapKeyArgumentResolver::class)
    private val keyDeserializer: Deserializer<K>,
    @Resolve(using = MapValueArgumentResolver::class)
    private val valueDeserializer: Deserializer<V>,
    @Resolve(using = DeserializationOrderArgumentResolver::class)
    private val order: DeserializationOrder,
    @Resolve(using = DeserializePostProcessorArgumentResolver::class)
    private val postProcessor: PostProcessor<Map<K, V>>
) : Deserializer<Map<K, V>> {

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE)
    annotation class MapDeserializerProperties(
        val deserializationOrder: DeserializationOrder
    )

    class DeserializationOrderArgumentResolver : ArgumentResolver<DeserializationOrder> {
        override fun resolve(deserializationContext: DeserializationContext): DeserializationOrder {
            val deserializerProperties = deserializationContext.getCurrentElement().getType().findAnnotation<MapDeserializerProperties>()
            return deserializerProperties
                ?.deserializationOrder
                ?: DeserializationOrder.KEY_FIRST
        }
    }

    enum class DeserializationOrder {
        KEY_FIRST,
        VALUE_FIRST
    }

    override fun deserialize(inputStream: CountingInputStream, deserializationContext: DeserializationContext): Map<K, V> {

        val numberOfMapEntries = inputStream.readUInt()

        val map = mutableMapOf<K, V>()
        for (i in 0u until numberOfMapEntries step 1) {
            val (key, value) = getKeyAndValue(inputStream, deserializationContext)
            map[key] = value
        }

        return map.also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }

    private fun getKeyAndValue(inputStream: CountingInputStream, deserializationContext: DeserializationContext): Pair<K, V> {
        return when (order) {
            DeserializationOrder.KEY_FIRST -> Pair(
                first = keyDeserializer.deserialize(inputStream, deserializationContext),
                second = valueDeserializer.deserialize(inputStream, deserializationContext)
            )

            DeserializationOrder.VALUE_FIRST -> Pair(
                second = valueDeserializer.deserialize(inputStream, deserializationContext),
                first = keyDeserializer.deserialize(inputStream, deserializationContext)
            )
        }
    }
}
