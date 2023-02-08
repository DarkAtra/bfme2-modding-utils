package de.darkatra.bfme2.map

import de.darkatra.bfme2.ConversionException

@Suppress("unused")
enum class TileFlammability(
    val byte: Byte
) {
    FIRE_RESISTANT(0),
    GRASS(1),
    HIGHLY_FLAMMABLE(2),
    UNDEFINED(3);

    companion object {
        fun ofByte(byte: Byte): TileFlammability {
            return values().find { it.byte == byte } ?: throw ConversionException("Unknown TileFlammability for byte '$byte'.")
        }
    }
}
