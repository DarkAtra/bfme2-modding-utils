package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.map.serialization.FourByteStringSerde
import de.darkatra.bfme2.map.serialization.Serialize

enum class CameraAnimationFrameInterpolationType(
    internal val rawName: @Serialize(using = FourByteStringSerde::class) String
) {
    CATM("MTAC"),
    LINE("ENIL")
}
