package de.darkatra.bfme2.map.blendtile

import de.darkatra.bfme2.ConversionException

enum class BlendDirection(
    internal val byte: Byte
) {
    BLEND_TOWARDS_RIGHT(1),     // Or towards left, if BlendDescription.Flags contains Flipped
    BLEND_TOWARDS_TOP(2),       // Or towards bottom, if BlendDescription.Flags contains Flipped
    BLEND_TOWARDS_TOP_RIGHT(4), // Or towards bottom left, if BlendDescription.Flags contains Flipped
    BLEND_TOWARDS_TOP_LEFT(8);  // Or towards bottom right, if BlendDescription.Flags contains Flipped

    internal companion object {
        fun ofByte(byte: Byte): BlendDirection {
            return values().find { it.byte == byte } ?: throw ConversionException("Unknown BlendDirection for byte '$byte'.")
        }
    }
}
