package de.darkatra.bfme2.map.writer

class MapFileComposeContext {

	private val assetNames: MutableMap<String, UInt> = mutableMapOf()

	fun getAssetNames() = assetNames

	fun getOrCreateAssetIndex(assetName: String): UInt {
		synchronized(assetNames) {
			return assetNames.getOrElse(assetName) {
				val nextAssetIndex = assetNames.size.toUInt() + 1u
				assetNames[assetName] = nextAssetIndex
				return nextAssetIndex
			}
		}
	}
}
