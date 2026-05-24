package de.darkatra.bfme2.w3d.model

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.readUInt

data class W3dChunkHeader(
    val type: W3dChunkType,
    val size: UInt,
    val end: ULong,
    val hasSubChunks: Boolean,
) {

    internal companion object {

        internal fun read(countingInputStream: CountingInputStream): W3dChunkHeader {

            val chunkTypeId = countingInputStream.readUInt()
            val rawChunkSize = countingInputStream.readUInt()
            val chunkSize = rawChunkSize and 0x7FFFFFFFu

            return W3dChunkHeader(
                type = W3dChunkType.ofTypeId(chunkTypeId),
                size = chunkSize,
                end = countingInputStream.count.toULong() + chunkSize,
                hasSubChunks = (rawChunkSize and 0x80000000u) != 0u,
            )
        }
    }
}
