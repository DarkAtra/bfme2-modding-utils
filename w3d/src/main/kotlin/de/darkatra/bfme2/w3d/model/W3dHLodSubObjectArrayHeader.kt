package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.ExperimentalApi
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import java.io.InputStream

@ExperimentalApi
data class W3dHLodSubObjectArrayHeader(
    @ExperimentalApi
    val modelCount: UInt,
    @ExperimentalApi
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
