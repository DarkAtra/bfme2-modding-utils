package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.map.serialization.Deserialize
import de.darkatra.bfme2.map.serialization.FourBitStringDeserializer

enum class CameraAnimationType(
    internal val rawName: @Deserialize(using = FourBitStringDeserializer::class) String
) {
    FREE("EERF"),
    LOOK("KOOL")
}
