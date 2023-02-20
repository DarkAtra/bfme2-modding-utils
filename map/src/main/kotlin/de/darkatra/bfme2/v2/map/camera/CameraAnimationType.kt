package de.darkatra.bfme2.v2.map.camera

import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.FourBitStringDeserializer

enum class CameraAnimationType(
    internal val rawName: @Deserialize(using = FourBitStringDeserializer::class) String
) {
    FREE("EERF"),
    LOOK("KOOL")
}
