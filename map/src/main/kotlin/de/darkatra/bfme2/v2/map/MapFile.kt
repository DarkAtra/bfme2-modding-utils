package de.darkatra.bfme2.v2.map

data class MapFile(
    val assetNameRegistry: AssetNameRegistry,
    val heightMapV5: HeightMapV5
) {

    class Builder {
        internal var assetNameRegistry: AssetNameRegistry? = null
        internal var heightMapV5: HeightMapV5? = null

        fun assetNameRegistry(assetNameRegistry: AssetNameRegistry) = apply { this.assetNameRegistry = assetNameRegistry }
        fun heightMapV5(heightMapV5: HeightMapV5) = apply { this.heightMapV5 = heightMapV5 }

        fun build() = MapFile(
            assetNameRegistry = assetNameRegistry ?: throwIllegalStateExceptionForField("assetNameRegistry"),
            heightMapV5 = heightMapV5 ?: throwIllegalStateExceptionForField("heightMapV5"),
        )

        private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
            throw IllegalStateException("Field '$fieldName' is null.")
        }
    }
}
