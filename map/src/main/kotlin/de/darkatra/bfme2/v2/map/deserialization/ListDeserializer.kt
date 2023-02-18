package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

@UseDeserializerProperties(ListDeserializer.Properties::class)
internal class ListDeserializer<T>(
    private val context: DeserializationContext,
    private val entryDeserializer: Deserializer<T>,
    private val postProcessor: PostProcessor<List<T>>,

    private val mode: Mode,
    private val sizeType: SizeType,
    private val size: UInt,
    private val sharedDataKey: String
) : Deserializer<List<T>> {

    init {
        if (mode == Mode.FIXED && size == 0u) {
            error("${ListDeserializer::class.simpleName} requires 'size' to be set via ${Properties::class.qualifiedName} when 'mode' is '${Mode.FIXED}'.")
        }
        if (mode == Mode.SHARED_DATA && sharedDataKey == "") {
            error("${ListDeserializer::class.simpleName} requires 'sharedDataKey' to be set via ${Properties::class.qualifiedName} when 'mode' is '${Mode.SHARED_DATA}'.")
        }
    }

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE)
    @DeserializerProperties
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
            list.add(entryDeserializer.deserialize(inputStream))
        }

        return list.also {
            postProcessor.postProcess(it, context)
        }
    }
}
