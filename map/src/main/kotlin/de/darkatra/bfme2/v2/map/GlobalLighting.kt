package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer.Mode

@Asset(name = "GlobalLighting", version = 8u)
data class GlobalLighting(
    val time: TimeOfDay,
    val lightingConfigurations: @ListDeserializer.Properties(mode = Mode.FIXED, size = 4u) List<GlobalLightingConfiguration>,
    val shadowColor: Color,
    val unknown: @ListDeserializer.Properties(mode = Mode.FIXED, size = 44u) List<Byte>,
    val noCloudFactor: Vector3
) {

    enum class TimeOfDay(
        internal val uInt: UInt
    ) {
        MORNING(1u),
        AFTERNOON(2u),
        EVENING(3u),
        NIGHT(4u)
    }

    data class GlobalLightingConfiguration(
        val terrainSun: GlobalLight,
        val terrainAccent1: GlobalLight,
        val terrainAccent2: GlobalLight,
        val objectSun: GlobalLight,
        val objectAccent1: GlobalLight,
        val objectAccent2: GlobalLight,
        val infantrySun: GlobalLight,
        val infantryAccent1: GlobalLight,
        val infantryAccent2: GlobalLight
    ) {

        data class GlobalLight(
            val ambient: Vector3,
            val color: Vector3,
            val direction: Vector3
        )
    }
}
