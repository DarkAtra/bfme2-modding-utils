package de.darkatra.bfme2.v2.map

data class Property(
    val key: PropertyKey,
    val value: Any
) {

    data class PropertyKey(
        val propertyType: PropertyType,
        val name: String
    ) {

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
    }
}
