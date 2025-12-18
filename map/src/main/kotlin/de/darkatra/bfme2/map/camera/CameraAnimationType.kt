package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.map.serialization.FourByteStringSerde
import de.darkatra.bfme2.map.serialization.Serialize
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_METHODS)
enum class CameraAnimationType(
    internal val rawName: @Serialize(using = FourByteStringSerde::class) String
) {
    FREE("EERF"),
    LOOK("KOOL")
}
