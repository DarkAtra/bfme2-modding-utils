package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.Camera
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class CamerasReader : AssetReader {

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

        MapFileReader.readAsset(reader, context, AssetName.NAMED_CAMERAS.assetName) {

            val numberOfNamedCameras = reader.readUInt()

            val cameras = mutableListOf<Camera>()
            for (i in 0u until numberOfNamedCameras step 1) {
                cameras.add(
                    readCamera(reader)
                )
            }

            builder.cameras(cameras)
        }
    }

    private fun readCamera(reader: CountingInputStream): Camera {
        return Camera(
            lookAtPoint = Vector3(
                x = reader.readFloat(),
                y = reader.readFloat(),
                z = reader.readFloat()
            ),
            name = reader.readUShortPrefixedString(),
            pitch = reader.readFloat(),
            roll = reader.readFloat(),
            yaw = reader.readFloat(),
            zoom = reader.readFloat(),
            fieldOfView = reader.readFloat(),
            unknown = reader.readFloat()
        )
    }
}
