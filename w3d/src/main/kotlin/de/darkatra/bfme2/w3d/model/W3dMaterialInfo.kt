package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.readUInt
import java.io.InputStream

data class W3dMaterialInfo(
    val passCount: UInt,
    val vertexMaterialCount: UInt,
    val shaderCount: UInt,
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
