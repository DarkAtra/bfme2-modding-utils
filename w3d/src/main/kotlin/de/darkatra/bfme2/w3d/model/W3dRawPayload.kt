package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.ExperimentalApi
import de.darkatra.bfme2.readNBytes
import java.io.InputStream

@ExperimentalApi
data class W3dRawPayload(
    @ExperimentalApi
    val bytes: List<Byte>,
) : W3dPayload {

    internal companion object {

        internal fun read(inputStream: InputStream, remaining: UInt): W3dRawPayload {

            return W3dRawPayload(
                bytes = inputStream.readNBytes(remaining).toList()
            )
        }
    }
}
