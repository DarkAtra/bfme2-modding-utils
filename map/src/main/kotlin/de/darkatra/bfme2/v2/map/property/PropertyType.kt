package de.darkatra.bfme2.v2.map.property

enum class PropertyType(
    internal val byte: Byte
) {
    BOOLEAN(0),
    INTEGER(1),
    FLOAT(2),
    ASCII_STRING(3),
    UNICODE_STRING(4),

    // seems to be the same as ASCII_STRING
    UNKNOWN(5)
}
