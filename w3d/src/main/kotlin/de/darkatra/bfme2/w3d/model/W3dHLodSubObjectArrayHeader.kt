package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import java.io.InputStream

data class W3dHLodSubObjectArrayHeader(
    val modelCount: UInt,
    val maxScreenSize: Float,
) : W3dPayload {

    internal companion object {

        internal fun read(inputStream: InputStream): W3dHLodSubObjectArrayHeader {

            return W3dHLodSubObjectArrayHeader(
                modelCount = inputStream.readUInt(),
                maxScreenSize = inputStream.readFloat(),
            )
        }
    }
}
