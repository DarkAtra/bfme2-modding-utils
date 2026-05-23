package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.readNBytes
import java.io.InputStream

data class W3dRawPayload(
    val bytes: List<Byte>,
) : W3dPayload {

    companion object {

        fun read(inputStream: InputStream, remaining: UInt): W3dRawPayload {

            return W3dRawPayload(
                bytes = inputStream.readNBytes(remaining).toList()
            )
        }
    }
}
