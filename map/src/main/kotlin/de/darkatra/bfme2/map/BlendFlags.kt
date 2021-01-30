package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException
import kotlin.experimental.or

enum class BlendFlags(
	val byte: Byte
) {
	NONE(0),
	FLIPPED(1),
	ALSO_HAS_BOTTOM_LEFT_OR_TOP_RIGHT_BLEND(2),
	FLIPPED_ALSO_HAS_BOTTOM_LEFT_OR_TOP_RIGHT_BLEND(FLIPPED.byte or ALSO_HAS_BOTTOM_LEFT_OR_TOP_RIGHT_BLEND.byte);

	companion object {
		fun ofByte(byte: Byte): BlendFlags {
			return values().find { it.byte == byte } ?: throw ConversionException("Unknown BlendFlags for byte '$byte'.")
		}
	}
}
