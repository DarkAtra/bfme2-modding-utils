package de.darkatra.bfme2.refpack

internal enum class Flags(
    internal val uByte: UByte
) {
    DEFAULT(0b00010000u), // always set
    STORES_COMPRESSED_SIZE(0b00000001u), // compressed size is added to header if set
    UNKNOWN(0b01000000u), // only used in the game "Spore"
    USE_32BIT_SIZE_HEADER(0b10000000u); // 4 bytes are used for the size if set, 3 bytes are used otherwise

    fun isPresent(uByte: UByte): Boolean {
        return uByte and this.uByte != UByte.MIN_VALUE
    }
}
