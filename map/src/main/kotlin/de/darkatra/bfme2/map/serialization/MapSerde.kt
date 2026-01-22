package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionHolder
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.writeUInt
import io.goodforgod.graalvm.hint.annotation.ReflectionHint
import java.io.OutputStream

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@UseSerdeProperties(MapSerde.Properties::class)
internal class MapSerde<K, V>(
    private val context: SerializationContext,
    serdes: List<Serde<*>>,
    private val preProcessor: PreProcessor<Map<K, V>>,
    private val postProcessor: PostProcessor<Map<K, V>>,

    private val order: DeserializationOrder
) : Serde<Map<K, V>> {

    private val keySerde: Serde<K>
    private val valueSerde: Serde<V>

    init {
        if (serdes.size != 2) {
            error("Expected exactly two serdes but found: $serdes")
        }

        @Suppress("UNCHECKED_CAST")
        keySerde = serdes[0] as Serde<K>
        @Suppress("UNCHECKED_CAST")
        valueSerde = serdes[1] as Serde<V>
    }

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE)
    @SerdeProperties
    @Suppress("unused") // properties are used via SerdePropertiesArgumentResolver
    internal annotation class Properties(
        val deserializationOrder: DeserializationOrder = DeserializationOrder.KEY_FIRST
    )

    internal enum class DeserializationOrder {
        KEY_FIRST,
        VALUE_FIRST
    }

    override fun calculateDataSection(data: Map<K, V>): DataSection {
        return DataSectionHolder(
            containingData = buildList {
                add(DataSectionLeaf.INT)
                data.entries.forEach { (key, value) ->
                    when (order) {
                        DeserializationOrder.KEY_FIRST -> {
                            add(keySerde.calculateDataSection(key))
                            add(valueSerde.calculateDataSection(value))
                        }

                        DeserializationOrder.VALUE_FIRST -> {
                            add(valueSerde.calculateDataSection(value))
                            add(keySerde.calculateDataSection(key))
                        }
                    }
                }
            }
        )
    }

    override fun serialize(outputStream: OutputStream, data: Map<K, V>) {

        preProcessor.preProcess(data, context).let { map ->

            outputStream.writeUInt(map.size.toUInt())

            map.forEach { (key, value) ->
                serializeKeyAndValue(outputStream, key, value)
            }
        }
    }

    override fun deserialize(inputStream: CountingInputStream): Map<K, V> {

        val numberOfMapEntries = inputStream.readUInt()

        val map = mutableMapOf<K, V>()
        for (i in 0u until numberOfMapEntries step 1) {
            val (key, value) = deserializeKeyAndValue(inputStream)
            map[key] = value
        }

        return map.also {
            postProcessor.postProcess(it, context)
        }
    }

    private fun serializeKeyAndValue(outputStream: OutputStream, key: K, value: V) {
        when (order) {
            DeserializationOrder.KEY_FIRST -> {
                keySerde.serialize(outputStream, key)
                valueSerde.serialize(outputStream, value)
            }

            DeserializationOrder.VALUE_FIRST -> {
                valueSerde.serialize(outputStream, value)
                keySerde.serialize(outputStream, key)
            }
        }
    }

    private fun deserializeKeyAndValue(inputStream: CountingInputStream): Pair<K, V> {
        return when (order) {
            DeserializationOrder.KEY_FIRST -> Pair(
                first = keySerde.deserialize(inputStream),
                second = valueSerde.deserialize(inputStream)
            )

            DeserializationOrder.VALUE_FIRST -> Pair(
                second = valueSerde.deserialize(inputStream),
                first = keySerde.deserialize(inputStream)
            )
        }
    }
}
