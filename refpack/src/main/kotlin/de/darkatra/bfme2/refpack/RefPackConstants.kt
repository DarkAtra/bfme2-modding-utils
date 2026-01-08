package de.darkatra.bfme2.refpack

internal object RefPackConstants {
    const val MAX_REFERENCED_DATA_DISTANCE = 131072
    const val MAX_BYTES_READ_COUNT = MAX_REFERENCED_DATA_DISTANCE * 300
    const val WINDOW_SIZE = MAX_REFERENCED_DATA_DISTANCE * 600
    const val MAGIC_HEADER_BYTE: UByte = 0xFBu
}
