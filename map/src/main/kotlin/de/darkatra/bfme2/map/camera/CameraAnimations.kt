package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.map.Asset
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "CameraAnimationList", version = 3u)
data class CameraAnimations(
    val animations: List<CameraAnimation>
)
