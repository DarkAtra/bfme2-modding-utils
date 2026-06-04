package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.ExperimentalApi
import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readNullTerminatedString
import de.darkatra.bfme2.readUInt
import java.io.InputStream

@ExperimentalApi
data class W3dMeshHeader(
    @ExperimentalApi
    val version: UInt,
    @ExperimentalApi
    val attributes: UInt,
    @ExperimentalApi
    val meshName: String,
    @ExperimentalApi
    val containerName: String,
    @ExperimentalApi
    val numTris: UInt,
    @ExperimentalApi
    val numVertices: UInt,
    @ExperimentalApi
    val numMaterials: UInt,
    @ExperimentalApi
    val numDamageStages: UInt,
    @ExperimentalApi
    val sortLevel: UInt,
    @ExperimentalApi
    val prelitVersion: UInt,
    @ExperimentalApi
    val futureCounts: UInt,
    @ExperimentalApi
    val vertexChannels: UInt,
    @ExperimentalApi
    val faceChannels: UInt,
    @ExperimentalApi
    val min: Vector3,
    @ExperimentalApi
    val max: Vector3,
    @ExperimentalApi
    val sphereCenter: Vector3,
    @ExperimentalApi
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
