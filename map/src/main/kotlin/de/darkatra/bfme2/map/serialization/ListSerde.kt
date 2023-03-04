package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.writeByte
import de.darkatra.bfme2.writeUInt
import de.darkatra.bfme2.writeUShort
import org.apache.commons.io.input.CountingInputStream
import java.io.OutputStream

@UseSerdeProperties(ListSerde.Properties::class)
internal class ListSerde<T>(
    private val context: SerializationContext,
    private val entrySerde: Serde<T>,
    private val preProcessor: PreProcessor<List<T>>,
    private val postProcessor: PostProcessor<List<T>>,

    private val mode: Mode,
    private val sizeType: SizeType,
    private val size: UInt,
    private val sharedDataKey: String
) : Serde<List<T>> {

    init {
        if (mode == Mode.FIXED && size == 0u) {
            error("${ListSerde::class.simpleName} requires 'size' to be set via ${Properties::class.qualifiedName} when 'mode' is '${Mode.FIXED}'.")
        }
        if (mode == Mode.SHARED_DATA && sharedDataKey == "") {
            error("${ListSerde::class.simpleName} requires 'sharedDataKey' to be set via ${Properties::class.qualifiedName} when 'mode' is '${Mode.SHARED_DATA}'.")
        }
    }

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE)
    @SerdeProperties
    @Suppress("unused") // properties are used via AnnotationParameterArgumentResolver
    internal annotation class Properties(
        val mode: Mode = Mode.DEFAULT,
        val sizeType: SizeType = SizeType.UINT,
        val size: UInt = 0u,
        val sharedDataKey: String = ""
    )

    internal enum class Mode {
        DEFAULT,
        FIXED,
        SHARED_DATA
    }

    internal enum class SizeType {
        UINT,
        USHORT,
        BYTE
    }

    override fun serialize(outputStream: OutputStream, data: List<T>) {

        preProcessor.preProcess(data, context).let { list ->

            val numberOfListEntries = list.size.toUInt()
            // TODO: validate list length for all modes and sizeTypes
            if (mode == Mode.DEFAULT) {
                when (sizeType) {
                    SizeType.UINT -> outputStream.writeUInt(numberOfListEntries)
                    SizeType.USHORT -> outputStream.writeUShort(numberOfListEntries.toUShort())
                    SizeType.BYTE -> outputStream.writeByte(numberOfListEntries.toByte())
                }
            }

            list.forEach { entry ->
                entrySerde.serialize(outputStream, entry)
            }
        }
    }

    override fun deserialize(inputStream: CountingInputStream): List<T> {

        val numberOfListEntries = when (mode) {
            Mode.DEFAULT -> when (sizeType) {
                SizeType.UINT -> inputStream.readUInt()
                SizeType.USHORT -> inputStream.readUShort().toUInt()
                SizeType.BYTE -> inputStream.readByte().toUInt()
            }

            Mode.FIXED -> size
            Mode.SHARED_DATA -> context.sharedData[sharedDataKey] as UInt
        }

        val list = mutableListOf<T>()
        for (i in 0u until numberOfListEntries step 1) {
            list.add(entrySerde.deserialize(inputStream))
        }

        return list.also {
            postProcessor.postProcess(it, context)
        }
    }
}
