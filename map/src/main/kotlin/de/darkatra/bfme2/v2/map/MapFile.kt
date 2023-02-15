package de.darkatra.bfme2.v2.map

data class MapFile(
    val assetNameRegistry: AssetNameRegistry,
    val blendTileDataV18: BlendTileDataV18,
    val heightMapV5: HeightMapV5,
    val multiplayerPositions: MultiplayerPositions,
    val worldInfo: WorldInfo
) {

    class Builder {
        internal var assetNameRegistry: AssetNameRegistry? = null
        internal var blendTileDataV18: BlendTileDataV18? = null
        internal var heightMapV5: HeightMapV5? = null
        internal var multiplayerPositions: MultiplayerPositions? = null
        internal var worldInfo: WorldInfo? = null

        fun assetNameRegistry(assetNameRegistry: AssetNameRegistry) = apply { this.assetNameRegistry = assetNameRegistry }
        fun blendTileDataV18(blendTileDataV18: BlendTileDataV18) = apply { this.blendTileDataV18 = blendTileDataV18 }
        fun heightMapV5(heightMapV5: HeightMapV5) = apply { this.heightMapV5 = heightMapV5 }
        fun multiplayerPositions(multiplayerPositions: MultiplayerPositions) = apply { this.multiplayerPositions = multiplayerPositions }
        fun worldInfo(worldInfo: WorldInfo) = apply { this.worldInfo = worldInfo }

        fun build() = MapFile(
            assetNameRegistry = assetNameRegistry ?: throwIllegalStateExceptionForField("assetNameRegistry"),
            blendTileDataV18 = blendTileDataV18 ?: throwIllegalStateExceptionForField("blendTileDataV18"),
            heightMapV5 = heightMapV5 ?: throwIllegalStateExceptionForField("heightMapV5"),
            multiplayerPositions = multiplayerPositions ?: throwIllegalStateExceptionForField("multiplayerPositions"),
            worldInfo = worldInfo ?: throwIllegalStateExceptionForField("worldInfo")
        )

        private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
            throw IllegalStateException("Field '$fieldName' is null.")
        }
    }
}
