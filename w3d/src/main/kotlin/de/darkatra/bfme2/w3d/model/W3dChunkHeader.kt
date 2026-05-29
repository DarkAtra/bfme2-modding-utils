package de.darkatra.bfme2.w3d.model

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.readUInt

data class W3dChunkHeader(
    val type: W3dChunkType,
    val start: UInt,
    val end: UInt,
    val hasSubChunks: Boolean,
) {

    val size: UInt by lazy {
        if (end < start) {
            throw InvalidDataException("Chunk end '$end' is before start '$start'")
        }
        end - start - (UInt.SIZE_BYTES.toUInt() * 2u)
    }

    internal companion object {

        internal fun read(countingInputStream: CountingInputStream): W3dChunkHeader {

            val chunkTypeId = countingInputStream.readUInt()
            val rawChunkSize = countingInputStream.readUInt()

            if (countingInputStream.count > UInt.MAX_VALUE.toLong()) {
                throw InvalidDataException("Current input stream position is greater than max allowed chunk offset of ${UInt.MAX_VALUE}")
            }

            val chunkStart = countingInputStream.count.toUInt()
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
