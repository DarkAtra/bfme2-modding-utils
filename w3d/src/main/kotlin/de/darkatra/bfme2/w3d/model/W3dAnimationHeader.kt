package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.ExperimentalApi
import de.darkatra.bfme2.readNullTerminatedString
import de.darkatra.bfme2.readUInt
import java.io.InputStream

@ExperimentalApi
data class W3dAnimationHeader(
    @ExperimentalApi
    val version: UInt,
    @ExperimentalApi
    val name: String,
    @ExperimentalApi
    val hierarchyName: String,
    @ExperimentalApi
    val numFrames: UInt,
    @ExperimentalApi
    val frameRate: UInt,
) : W3dPayload {

    internal companion object {

        internal fun read(inputStream: InputStream): W3dAnimationHeader {

            return W3dAnimationHeader(
                version = inputStream.readUInt(),
                name = inputStream.readNullTerminatedString(fixedLength = 16u),
                hierarchyName = inputStream.readNullTerminatedString(fixedLength = 16u),
                numFrames = inputStream.readUInt(),
                frameRate = inputStream.readUInt(),
            )
        }
    }
}
