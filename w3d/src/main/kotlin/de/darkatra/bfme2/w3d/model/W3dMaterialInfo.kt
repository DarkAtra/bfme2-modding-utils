package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.ExperimentalApi
import de.darkatra.bfme2.readUInt
import java.io.InputStream

@ExperimentalApi
data class W3dMaterialInfo(
    @ExperimentalApi
    val passCount: UInt,
    @ExperimentalApi
    val vertexMaterialCount: UInt,
    @ExperimentalApi
    val shaderCount: UInt,
    @ExperimentalApi
    val textureCount: UInt
) : W3dPayload {

    internal companion object {

        internal fun read(inputStream: InputStream): W3dMaterialInfo {

            return W3dMaterialInfo(
                passCount = inputStream.readUInt(),
                vertexMaterialCount = inputStream.readUInt(),
                shaderCount = inputStream.readUInt(),
                textureCount = inputStream.readUInt(),
            )
        }
    }
}
