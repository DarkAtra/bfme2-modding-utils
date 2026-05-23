package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.readNullTerminatedString
import de.darkatra.bfme2.readUInt
import java.io.InputStream

data class W3dHLodSubObject(
    val boneIndex: UInt,
    val name: String,
) : W3dPayload {

    companion object {

        internal fun read(inputStream: InputStream): W3dHLodSubObject {

            return W3dHLodSubObject(
                boneIndex = inputStream.readUInt(),
                name = inputStream.readNullTerminatedString(fixedLength = 32u),
            )
        }
    }
}
