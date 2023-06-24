package de.darkatra.bfme2.map.serialization

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import de.darkatra.bfme2.map.heightmap.HeightMap
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.writeByte
import org.apache.commons.io.input.CountingInputStream
import java.io.OutputStream
import kotlin.experimental.and
import kotlin.experimental.or

@UseSerdeProperties(HeightMapDependentMapSerde.Properties::class)
internal class HeightMapDependentMapSerde<V : Any>(
    private val context: SerializationContext,
    private val valueSerde: Serde<V>,
    private val preProcessor: PreProcessor<Table<UInt, UInt, V>>,
    private val postProcessor: PostProcessor<Table<UInt, UInt, V>>,

    private val mode: Mode
) : Serde<Table<UInt, UInt, V>> {

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE)
    @SerdeProperties
    @Suppress("unused") // properties are used via AnnotationParameterArgumentResolver
    internal annotation class Properties(
        val mode: Mode = Mode.DEFAULT
    )

    internal enum class Mode {
        DEFAULT,
        SAGE_BOOLEAN
    }

    override fun calculateByteCount(data: Table<UInt, UInt, V>): Long {

        val width = data.rowKeySet().size.toUInt()
        val height = data.columnKeySet().size.toUInt()

        return (0u until width step 1).sumOf { x ->
            (0u until height step 1).sumOf { y ->
                when (mode) {
                    Mode.DEFAULT -> valueSerde.calculateByteCount(data[x, y]!!)
                    else -> when (x > 0u && x % 8u == 0u) {
                        true -> 1
                        else -> 0
                    }
                }
            }
        }
    }

    override fun serialize(outputStream: OutputStream, data: Table<UInt, UInt, V>) {

        preProcessor.preProcess(data, context).let { map ->

            val width = map.rowKeySet().size.toUInt()
            val height = map.columnKeySet().size.toUInt()

            for (x in 0u until width step 1) {
                var temp = 0.toByte()
                for (y in 0u until height step 1) {
                    when (mode) {
                        Mode.DEFAULT -> valueSerde.serialize(outputStream, map[x, y]!!)
                        else -> {
                            if (x > 0u && x % 8u == 0u) {
                                outputStream.writeByte(temp)
                            }
                            temp = temp or ((if (map[x, y] as Boolean) 1 else 0) shl (x % 8u).toInt()).toByte()
                        }
                    }
                }
            }
        }
    }

    override fun deserialize(inputStream: CountingInputStream): Table<UInt, UInt, V> {

        val width = context.sharedData[HeightMap.HEIGHT_MAP_WIDTH] as UInt
        val height = context.sharedData[HeightMap.HEIGHT_MAP_HEIGHT] as UInt

        val map = HashBasedTable.create<UInt, UInt, V>(width.toInt(), height.toInt())
        for (x in 0u until width step 1) {
            var temp = 0.toByte()
            for (y in 0u until height step 1) {
                when (mode) {
                    Mode.DEFAULT -> map.put(x, y, valueSerde.deserialize(inputStream))
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
