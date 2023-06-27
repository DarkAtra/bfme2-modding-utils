package de.darkatra.bfme2.map.serialization

internal data class AssetEntry(
    internal val assetName: String,
    internal val assetVersion: UShort,
    internal val assetSize: Long,
    internal val startPosition: Long
) {
    internal val endPosition: Long
        get() = startPosition + assetSize
}
