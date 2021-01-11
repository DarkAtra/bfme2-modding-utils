package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException

enum class BlendDirection {
	BLEND_TOWARDS_RIGHT,     // Or towards left, if BlendDescription.Flags contains Flipped
	BLEND_TOWARDS_TOP,       // Or towards bottom, if BlendDescription.Flags contains Flipped
	BLEND_TOWARDS_TOP_RIGHT, // Or towards bottom left, if BlendDescription.Flags contains Flipped
	BLEND_TOWARDS_TOP_LEFT;  // Or towards bottom right, if BlendDescription.Flags contains Flipped

	companion object {

		fun ofByte(byte: Byte): BlendDirection {
			return when (byte) {
				1.toByte() -> BLEND_TOWARDS_RIGHT
				2.toByte() -> BLEND_TOWARDS_TOP
				4.toByte() -> BLEND_TOWARDS_TOP_RIGHT
				8.toByte() -> BLEND_TOWARDS_TOP_LEFT
				else -> throw ConversionException("Unknown BlendDirection for byte '$byte'.")
			}
		}
	}
}
