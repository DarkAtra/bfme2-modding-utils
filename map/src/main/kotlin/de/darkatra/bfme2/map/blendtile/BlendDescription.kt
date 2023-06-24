package de.darkatra.bfme2.map.blendtile

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.serialization.ListSerde
import de.darkatra.bfme2.map.serialization.SerializationContext
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcess
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import kotlin.experimental.or

@PostProcess(using = BlendDescription.BlendDescriptionPostProcessor::class)
data class BlendDescription(
    val secondaryTextureTile: UInt,
    internal val rawBlendDirection: @ListSerde.Properties(mode = ListSerde.Mode.FIXED, size = 4u) List<Byte>,
    val flags: BlendFlags,
    val twoSided: Boolean,
    val magicValue1: UInt,
    val magicValue2: UInt
) {

    @Suppress("unused") // public api
    val blendDirection: BlendDirection
        get() {
            val bytes = rawBlendDirection.toTypedArray()
            var result: Byte = 0
            for (i in bytes.indices) {
                if (bytes[i] != 0.toByte() && bytes[i] != 1.toByte()) {
                    throw NotImplementedError("BlendDirection conversion not fully implemented yet.")
                }
                if (bytes[i] != 0.toByte()) {
                    result = result or (bytes[i].toInt() shl i).toByte()
                }
            }
            return BlendDirection.ofByte(result)
        }

    internal class BlendDescriptionPostProcessor : PostProcessor<BlendDescription> {
        override fun postProcess(data: BlendDescription, context: SerializationContext) {
            if (data.magicValue1 != 0xffffffffu) {
                throw InvalidDataException("Expected magicValue1 to be '0xffffffffu'. Found: ${data.magicValue1.toString(16)}")
            }
            if (data.magicValue2 != 0x7ada0000u) {
                throw InvalidDataException("Expected magicValue1 to be '0x7ada0000u'. Found: ${data.magicValue2.toString(16)}")
            }
        }
    }
}
