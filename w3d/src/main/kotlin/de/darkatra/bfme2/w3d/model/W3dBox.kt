package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readNullTerminatedString
import de.darkatra.bfme2.readUInt
import java.io.InputStream

data class W3dBox(
    val version: UInt,
    val boxType: W3dBoxType,
    val collisionFlags: W3dBoxCollisionType,
    val name: String,
    val color: Color,
    val center: Vector3,
    val extent: Vector3,
) : W3dPayload {

    companion object {

        internal fun read(inputStream: InputStream): W3dBox {

            val version = inputStream.readUInt()
            val flags = inputStream.readUInt()
            val name = inputStream.readNullTerminatedString(fixedLength = 16u)
            val r = inputStream.readByte().toUInt()
            val g = inputStream.readByte().toUInt()
            val b = inputStream.readByte().toUInt()
            val unusedAlpha = inputStream.readByte()
            if (unusedAlpha != 0.toByte()) {
                throw InvalidDataException("Expected unusedAlpha to be 0 reading W3dBox.")
            }

            return W3dBox(
                version = version,
                boxType = W3dBoxType.ofUByte((flags and 0b11u).toUByte()),
                collisionFlags = W3dBoxCollisionType.ofUInt(flags and 0xFF0u),
                name = name,
                color = Color(
                    red = r,
                    green = g,
                    blue = b,
                ),
                center = Vector3(
                    x = inputStream.readFloat(),
                    y = inputStream.readFloat(),
                    z = inputStream.readFloat()
                ),
                extent = Vector3(
                    x = inputStream.readFloat(),
                    y = inputStream.readFloat(),
                    z = inputStream.readFloat()
                ),
            )
        }
    }
}
