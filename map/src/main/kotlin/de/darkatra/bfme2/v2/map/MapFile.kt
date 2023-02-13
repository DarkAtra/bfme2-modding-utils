package de.darkatra.bfme2.v2.map

data class MapFile(
    val assetNameRegistry: AssetNameRegistry,
    val heightMapV5: HeightMapV5,
    val worldInfo: WorldInfo
) {

    class Builder {
        internal var assetNameRegistry: AssetNameRegistry? = null
        internal var heightMapV5: HeightMapV5? = null
        internal var worldInfo: WorldInfo? = null

        fun assetNameRegistry(assetNameRegistry: AssetNameRegistry) = apply { this.assetNameRegistry = assetNameRegistry }
        fun heightMapV5(heightMapV5: HeightMapV5) = apply { this.heightMapV5 = heightMapV5 }
        fun worldInfo(worldInfo: WorldInfo) = apply { this.worldInfo = worldInfo }

        fun build() = MapFile(
            assetNameRegistry = assetNameRegistry ?: throwIllegalStateExceptionForField("assetNameRegistry"),
            heightMapV5 = heightMapV5 ?: throwIllegalStateExceptionForField("heightMapV5"),
            worldInfo = worldInfo ?: throwIllegalStateExceptionForField("worldInfo")
        )

        private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
            throw IllegalStateException("Field '$fieldName' is null.")
        }
    }
}
