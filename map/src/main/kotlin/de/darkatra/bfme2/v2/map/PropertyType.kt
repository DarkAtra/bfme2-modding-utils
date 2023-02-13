package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.ConversionException

enum class PropertyType {
    BOOLEAN,
    INTEGER,
    FLOAT,
    ASCII_STRING,
    UNICODE_STRING,

    // seems to be the same as ASCII_STRING
    UNKNOWN;

    companion object {

        fun ofByte(byte: Byte): PropertyType {
            return when (byte) {
                0.toByte() -> BOOLEAN
                1.toByte() -> INTEGER
                2.toByte() -> FLOAT
                3.toByte() -> ASCII_STRING
                4.toByte() -> UNICODE_STRING
                5.toByte() -> UNKNOWN
                else -> throw ConversionException("Unknown PropertyType for byte '$byte'.")
            }
        }
    }
}
