package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readNullTerminatedString
import de.darkatra.bfme2.readUInt
import java.io.InputStream

data class W3dMeshHeader(
    val version: UInt,
    val attributes: UInt,
    val meshName: String,
    val containerName: String,
    val numTris: UInt,
    val numVertices: UInt,
    val numMaterials: UInt,
    val numDamageStages: UInt,
    val sortLevel: UInt,
    val prelitVersion: UInt,
    val futureCounts: UInt,
    val vertexChannels: UInt,
    val faceChannels: UInt,
    val min: Vector3,
    val max: Vector3,
    val sphereCenter: Vector3,
    val sphereRadius: Float,
) : W3dPayload {

    internal companion object {

        internal fun read(inputStream: InputStream): W3dMeshHeader {

            return W3dMeshHeader(
                version = inputStream.readUInt(),
                attributes = inputStream.readUInt(),
                meshName = inputStream.readNullTerminatedString(fixedLength = 16u),
                containerName = inputStream.readNullTerminatedString(fixedLength = 16u),
                numTris = inputStream.readUInt(),
                numVertices = inputStream.readUInt(),
                numMaterials = inputStream.readUInt(),
                numDamageStages = inputStream.readUInt(),
                sortLevel = inputStream.readUInt(),
                prelitVersion = inputStream.readUInt(),
                futureCounts = inputStream.readUInt(),
                vertexChannels = inputStream.readUInt(),
                faceChannels = inputStream.readUInt(),
                min = Vector3(
                    x = inputStream.readFloat(),
                    y = inputStream.readFloat(),
                    z = inputStream.readFloat()
                ),
                max = Vector3(
                    x = inputStream.readFloat(),
                    y = inputStream.readFloat(),
                    z = inputStream.readFloat()
                ),
                sphereCenter = Vector3(
                    x = inputStream.readFloat(),
                    y = inputStream.readFloat(),
                    z = inputStream.readFloat()
                ),
                sphereRadius = inputStream.readFloat(),
            )
        }
    }
}
