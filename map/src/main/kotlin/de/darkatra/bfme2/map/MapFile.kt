package de.darkatra.bfme2.map

import de.darkatra.bfme2.map.asset.AssetList
import de.darkatra.bfme2.map.asset.BuildLists
import de.darkatra.bfme2.map.asset.WorldInfo

data class MapFile(
	private val assetList: AssetList,
	private val buildLists: BuildLists,
	private val worldInfo: WorldInfo
) {
	class Builder {
		var assetList: AssetList? = null
			private set
		var buildLists: BuildLists? = null
			private set
		var worldInfo: WorldInfo? = null
			private set

		fun assetList(assetList: AssetList) = apply { this.assetList = assetList }
		fun buildLists(buildLists: BuildLists) = apply { this.buildLists = buildLists }
		fun worldInfo(worldInfo: WorldInfo) = apply { this.worldInfo = worldInfo }

		fun build() = MapFile(
			assetList = assetList!!,
			buildLists = buildLists!!,
			worldInfo = worldInfo!!
		)
	}
}
