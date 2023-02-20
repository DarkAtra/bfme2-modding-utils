package de.darkatra.bfme2.v2.map.blendtile

import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer
import kotlin.experimental.or

// TODO: implement validator
data class BlendDescription(
    val secondaryTextureTile: UInt,
    private val rawBlendDirection: @ListDeserializer.Properties(mode = ListDeserializer.Mode.FIXED, size = 4u) List<Byte>,
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
}
