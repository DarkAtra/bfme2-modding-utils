package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.map.serialization.FourByteStringSerde
import de.darkatra.bfme2.map.serialization.Serialize

enum class CameraAnimationType(
    internal val rawName: @Serialize(using = FourByteStringSerde::class) String
) {
    FREE("EERF"),
    LOOK("KOOL")
}
