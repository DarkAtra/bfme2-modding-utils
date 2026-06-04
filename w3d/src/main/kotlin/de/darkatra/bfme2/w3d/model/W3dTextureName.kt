package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.ExperimentalApi
import de.darkatra.bfme2.readNullTerminatedString
import java.io.InputStream

@ExperimentalApi
data class W3dTextureName(
    @ExperimentalApi
    val value: String,
) : W3dPayload {

    internal companion object {

        internal fun read(inputStream: InputStream, length: UInt): W3dTextureName {

            return W3dTextureName(
                value = inputStream.readNullTerminatedString(fixedLength = length),
            )
        }
    }
}
