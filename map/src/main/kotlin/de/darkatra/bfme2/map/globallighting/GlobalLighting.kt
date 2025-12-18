package de.darkatra.bfme2.map.globallighting

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.ListSerde
import de.darkatra.bfme2.map.serialization.ListSerde.Mode
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "GlobalLighting", version = 8u)
data class GlobalLighting(
    val time: TimeOfDay,
    val lightingConfigurations: @ListSerde.Properties(mode = Mode.FIXED, size = 4u) List<GlobalLightingConfiguration>,
    val shadowColor: Color,
    val unknown: @ListSerde.Properties(mode = Mode.FIXED, size = 44u) List<Byte>,
    val noCloudFactor: Vector3
)
