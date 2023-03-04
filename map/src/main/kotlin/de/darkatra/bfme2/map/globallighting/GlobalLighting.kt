package de.darkatra.bfme2.map.globallighting

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.ListDeserializer
import de.darkatra.bfme2.map.serialization.ListDeserializer.Mode

@Asset(name = "GlobalLighting", version = 8u)
data class GlobalLighting(
    val time: TimeOfDay,
    val lightingConfigurations: @ListDeserializer.Properties(mode = Mode.FIXED, size = 4u) List<GlobalLightingConfiguration>,
    val shadowColor: Color,
    val unknown: @ListDeserializer.Properties(mode = Mode.FIXED, size = 44u) List<Byte>,
    val noCloudFactor: Vector3
)
