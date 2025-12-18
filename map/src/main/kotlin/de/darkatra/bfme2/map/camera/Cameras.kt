package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.map.Asset
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "NamedCameras", version = 2u)
data class Cameras(
    val cameras: List<Camera>
)
