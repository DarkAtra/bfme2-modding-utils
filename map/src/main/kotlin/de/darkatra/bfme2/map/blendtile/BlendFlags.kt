package de.darkatra.bfme2.map.blendtile

import kotlin.experimental.or

enum class BlendFlags(
    internal val byte: Byte
) {
    NONE(0),
    FLIPPED(1),
    ALSO_HAS_BOTTOM_LEFT_OR_TOP_RIGHT_BLEND(2),
    FLIPPED_ALSO_HAS_BOTTOM_LEFT_OR_TOP_RIGHT_BLEND(FLIPPED.byte or ALSO_HAS_BOTTOM_LEFT_OR_TOP_RIGHT_BLEND.byte)
}
