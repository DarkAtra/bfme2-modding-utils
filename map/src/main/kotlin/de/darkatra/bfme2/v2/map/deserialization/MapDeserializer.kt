package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

@UseDeserializerProperties(MapDeserializer.MapDeserializerProperties::class)
internal class MapDeserializer<K, V>(
    private val context: DeserializationContext,
    private val deserializers: List<Deserializer<*>>,
    private val postProcessor: PostProcessor<Map<K, V>>,

    private val order: DeserializationOrder
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
    @DeserializerProperties
    @Suppress("unused") // properties are used via AnnotationParameterArgumentResolver
    annotation class MapDeserializerProperties(
        val deserializationOrder: DeserializationOrder
    )

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
