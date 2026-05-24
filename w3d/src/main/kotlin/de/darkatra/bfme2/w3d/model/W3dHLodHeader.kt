package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.readNullTerminatedString
import de.darkatra.bfme2.readUInt
import java.io.InputStream

data class W3dHLodHeader(
    val version: UInt,
    val lodCount: UInt,
    val name: String,
    val hierarchyName: String,
) : W3dPayload {

    internal companion object {

        internal fun read(inputStream: InputStream): W3dHLodHeader {

            return W3dHLodHeader(
                version = inputStream.readUInt(),
                lodCount = inputStream.readUInt(),
                name = inputStream.readNullTerminatedString(fixedLength = 16u),
                hierarchyName = inputStream.readNullTerminatedString(fixedLength = 16u),
            )
        }
    }
}
