package de.darkatra.bfme2.w3d.model

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.readUInt

data class W3dChunkHeader(
    val type: W3dChunkType,
    val start: ULong,
    val end: ULong,
    val hasSubChunks: Boolean,
) {

    val size: UInt by lazy {
        val size = end - start - (UInt.SIZE_BYTES.toUInt() * 2u)
        if (size > UInt.MAX_VALUE) {
            throw InvalidDataException("Chunk is larger than the max. allowed size of ${UInt.MAX_VALUE}")
        }
        size.toUInt()
    }

    internal companion object {

        internal fun read(countingInputStream: CountingInputStream): W3dChunkHeader {

            val chunkTypeId = countingInputStream.readUInt()
            val rawChunkSize = countingInputStream.readUInt()
            val chunkStart = countingInputStream.count.toULong()
            val chunkSize = rawChunkSize and 0x7FFFFFFFu

            return W3dChunkHeader(
                type = W3dChunkType.ofTypeId(chunkTypeId),
                start = chunkStart - (UInt.SIZE_BYTES.toUInt() * 2u),
                end = chunkStart + chunkSize,
                hasSubChunks = (rawChunkSize and 0x80000000u) != 0u,
            )
        }
    }
}
