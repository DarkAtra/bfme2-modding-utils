package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.ExperimentalApi
import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readNullTerminatedString
import de.darkatra.bfme2.readUInt
import java.io.InputStream

@ExperimentalApi
data class W3dHierarchyHeader(
    @ExperimentalApi
    val version: UInt,
    @ExperimentalApi
    val name: String,
    @ExperimentalApi
    val numPivots: UInt,
    @ExperimentalApi
    val center: Vector3,
) : W3dPayload {

    internal companion object {

        internal fun read(inputStream: InputStream): W3dHierarchyHeader {

            return W3dHierarchyHeader(
                version = inputStream.readUInt(),
                name = inputStream.readNullTerminatedString(fixedLength = 16u),
                numPivots = inputStream.readUInt(),
                center = Vector3(
                    x = inputStream.readFloat(),
                    y = inputStream.readFloat(),
                    z = inputStream.readFloat(),
                ),
            )
        }
    }
}
