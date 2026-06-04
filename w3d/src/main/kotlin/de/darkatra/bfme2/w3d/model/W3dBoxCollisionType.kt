package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.ConversionException
import de.darkatra.bfme2.ExperimentalApi

@ExperimentalApi
enum class W3dBoxCollisionType(
    internal val uInt: UInt,
) {

    NONE(0u),
    PHYSICAL(0x10u),
    PROJECTILE(0x20u),
    VIS(0x40u),
    CAMERA(0x80u),
    VEHICLE(0x100u);

    internal companion object {
        fun ofUInt(uInt: UInt): W3dBoxCollisionType {
            return entries.find { it.uInt == uInt }
                ?: throw ConversionException("Could not deserialize W3dBoxCollisionType from '$uInt' (UInt)")
        }
    }
}
