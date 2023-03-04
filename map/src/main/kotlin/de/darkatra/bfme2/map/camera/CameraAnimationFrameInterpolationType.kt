package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.map.serialization.Deserialize
import de.darkatra.bfme2.map.serialization.FourBitStringDeserializer

enum class CameraAnimationFrameInterpolationType(
    internal val rawName: @Deserialize(using = FourBitStringDeserializer::class) String
) {
    CATM("MTAC"),
    LINE("ENIL")
}
