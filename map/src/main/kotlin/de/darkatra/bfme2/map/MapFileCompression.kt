package de.darkatra.bfme2.map

enum class MapFileCompression(
    internal val fourCC: String
) {
    UNCOMPRESSED("CkMp"),
    REFPACK("EAR\u0000"),
    ZLIB("ZL5\u0000")
}
