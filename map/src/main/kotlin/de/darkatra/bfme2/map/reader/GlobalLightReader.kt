package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.GlobalLight
import de.darkatra.bfme2.readFloat
import org.apache.commons.io.input.CountingInputStream

class GlobalLightReader {

    fun read(reader: CountingInputStream): GlobalLight {
        return GlobalLight(
            ambient = Vector3(
                x = reader.readFloat(),
                y = reader.readFloat(),
                z = reader.readFloat()
            ),
            color = Vector3(
                x = reader.readFloat(),
                y = reader.readFloat(),
                z = reader.readFloat()
            ),
            direction = Vector3(
                x = reader.readFloat(),
                y = reader.readFloat(),
                z = reader.readFloat()
            )
        )
    }
}
