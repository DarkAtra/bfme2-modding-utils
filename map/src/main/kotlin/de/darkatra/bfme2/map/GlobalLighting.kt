package de.darkatra.bfme2.map

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.Vector3
import java.util.EnumMap

data class GlobalLighting(
    val time: TimeOfDay,
    val lightingConfigurations: EnumMap<TimeOfDay, GlobalLightingConfiguration>,
    val shadowColor: Color,
    val unknown: List<Byte>,
    val unknown2: Vector3?,
    val unknown3: Color?,
    val noCloudFactor: Vector3?
) {

    class Builder {
        private var time: TimeOfDay? = null
        private var lightingConfigurations: EnumMap<TimeOfDay, GlobalLightingConfiguration>? = null
        private var shadowColor: Color? = null
        private var unknown: List<Byte>? = null
        private var unknown2: Vector3? = null
        private var unknown3: Color? = null
        private var noCloudFactor: Vector3? = null

        fun time(time: TimeOfDay) = apply { this.time = time }
        fun lightingConfigurations(lightingConfigurations: EnumMap<TimeOfDay, GlobalLightingConfiguration>) =
            apply { this.lightingConfigurations = lightingConfigurations }

        fun shadowColor(shadowColor: Color) = apply { this.shadowColor = shadowColor }
        fun unknown(unknown: List<Byte>) = apply { this.unknown = unknown }
        fun unknown2(unknown2: Vector3) = apply { this.unknown2 = unknown2 }
        fun unknown3(unknown3: Color) = apply { this.unknown3 = unknown3 }
        fun noCloudFactor(noCloudFactor: Vector3) = apply { this.noCloudFactor = noCloudFactor }

        fun build() = GlobalLighting(
            time = time ?: throwIllegalStateExceptionForField("time"),
            lightingConfigurations = lightingConfigurations ?: throwIllegalStateExceptionForField("lightingConfigurations"),
            shadowColor = shadowColor ?: throwIllegalStateExceptionForField("shadowColor"),
            unknown = unknown ?: throwIllegalStateExceptionForField("unknown"),
            unknown2 = unknown2,
            unknown3 = unknown3,
            noCloudFactor = noCloudFactor
        )

        private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
            throw IllegalStateException("Field '$fieldName' is null.")
        }
    }
}
