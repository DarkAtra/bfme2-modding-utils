package de.darkatra.bfme2.assetdat.model

data class AssetDatFile(
    val assets: List<Asset>,
) {

    companion object {
        const val FOUR_CC: String = "ALAE"
        const val VERSION: UInt = 0x102u
    }

    fun merge(other: AssetDatFile): AssetDatFile {

        val assets = assets.toMutableList()

        for (asset in other.assets) {
            assets.removeIf { it.name == asset.name }
            assets.add(asset)
        }

        return AssetDatFile(
            assets = assets.sortedBy { it.name }
        )
    }
}
