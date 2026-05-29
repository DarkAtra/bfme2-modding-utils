package de.darkatra.bfme2.assetdat.model

import de.darkatra.bfme2.ConversionException

enum class AssetEntryKind(
    internal val uInt: UInt,
    internal val allowsDependencies: Boolean,
) {

    MESH(0x4D455348u, true),
    ANIMATION(0x414E494Du, false),
    HLOD(0x484C4F44u, true),
    TEXTURE(0x00544558u, false),
    HIERARCHY(0x48494552u, false),
    BOX(0x00424F58u, false),
    FX_SHADER(0x46585348u, false),
    PARTICLE(0x50415254u, true);

    internal companion object {
        internal fun ofUInt(uInt: UInt): AssetEntryKind {
            return entries.find { it.uInt == uInt }
                ?: throw ConversionException("Could not deserialize DependencyKind from '$uInt' (UInt)")
        }
    }
}
