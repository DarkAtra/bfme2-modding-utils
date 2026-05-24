package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.readNullTerminatedString
import java.io.InputStream

data class W3dVertexMaterialName(
    val value: String,
) : W3dPayload {

    internal companion object {

        internal fun read(inputStream: InputStream, length: UInt): W3dVertexMaterialName {

            return W3dVertexMaterialName(
                value = inputStream.readNullTerminatedString(fixedLength = length)
            )
        }
    }
}
