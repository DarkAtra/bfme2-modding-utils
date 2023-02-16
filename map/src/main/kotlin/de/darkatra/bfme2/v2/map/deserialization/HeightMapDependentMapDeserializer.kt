package de.darkatra.bfme2.v2.map.deserialization

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.v2.map.HeightMap
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import kotlin.experimental.and

@UseDeserializerProperties(HeightMapDependentMapDeserializer.Properties::class)
internal class HeightMapDependentMapDeserializer<V : Any>(
    private val context: DeserializationContext,
    private val valueDeserializer: Deserializer<V>,
    private val postProcessor: PostProcessor<Table<UInt, UInt, V>>,

    private val mode: Mode
) : Deserializer<Table<UInt, UInt, V>> {

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE)
    @DeserializerProperties
    @Suppress("unused") // properties are used via AnnotationParameterArgumentResolver
    internal annotation class Properties(
        val mode: Mode = Mode.DEFAULT
    )

    internal enum class Mode {
        DEFAULT,
        SAGE_BOOLEAN
    }

    // TODO: consider using Map<UInt, Map<UInt, V>> instead of Table here to remove the dependency on guava
    override fun deserialize(inputStream: CountingInputStream): Table<UInt, UInt, V> {

        val width = context.sharedData[HeightMap.HEIGHT_MAP_WIDTH] as UInt
        val height = context.sharedData[HeightMap.HEIGHT_MAP_HEIGHT] as UInt

        val map = HashBasedTable.create<UInt, UInt, V>(width.toInt(), height.toInt())
        for (x in 0u until width step 1) {
            var temp = 0.toByte()
            for (y in 0u until height step 1) {
                when (mode) {
                    Mode.DEFAULT -> map.put(x, y, valueDeserializer.deserialize(inputStream))
                    else -> {
                        if (x % 8u == 0u) {
                            temp = inputStream.readByte()
                        }
                        @Suppress("UNCHECKED_CAST")
                        map.put(x, y, (temp and (1u shl (x % 8u).toInt()).toByte() != 0.toByte()) as V)
                    }
                }
            }
        }

        return map.also {
            postProcessor.postProcess(it, context)
        }
    }
}
