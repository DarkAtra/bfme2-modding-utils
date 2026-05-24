package de.darkatra.bfme2.assetdat.model

data class AssetDatFile(
    val assets: List<Asset>,
) {

    companion object {
        const val FOUR_CC: String = "ALAE"
        const val VERSION: UInt = 0x102u
    }
}
