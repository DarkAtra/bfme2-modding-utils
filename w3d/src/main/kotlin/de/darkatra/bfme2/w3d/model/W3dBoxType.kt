package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.ConversionException

enum class W3dBoxType(
    internal val uByte: UByte
) {

    ORIENTED(1u),
    ALIGNED(2u);

    internal companion object {
        fun ofUByte(uByte: UByte): W3dBoxType {
            return entries.find { it.uByte == uByte }
                ?: throw ConversionException("Could not deserialize W3dBoxType from '$uByte' (UByte)")
        }
    }
}
