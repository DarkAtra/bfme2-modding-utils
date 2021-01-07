package de.darkatra.bfme2.map

import de.darkatra.bfme2.map.asset.AssetList
import de.darkatra.bfme2.map.asset.BuildLists
import de.darkatra.bfme2.map.asset.GlobalVersion
import de.darkatra.bfme2.map.asset.MPPositionList
import de.darkatra.bfme2.map.asset.WorldInfo

data class MapFile(
	private val assetList: AssetList,
	private val buildLists: BuildLists,
	private val globalVersion: GlobalVersion,
	private val mpPositionList: MPPositionList,
	private val worldInfo: WorldInfo
) {

	class Builder {
		var assetList: AssetList? = null
			private set
		var buildLists: BuildLists? = null
			private set
		var globalVersion: GlobalVersion? = null
			private set
		var mpPositionList: MPPositionList? = null
			private set
		var worldInfo: WorldInfo? = null
			private set

		fun assetList(assetList: AssetList) = apply { this.assetList = assetList }
		fun buildLists(buildLists: BuildLists) = apply { this.buildLists = buildLists }
		fun globalVersion(globalVersion: GlobalVersion) = apply { this.globalVersion = globalVersion }
		fun mpPositionList(mpPositionList: MPPositionList) = apply { this.mpPositionList = mpPositionList }
		fun worldInfo(worldInfo: WorldInfo) = apply { this.worldInfo = worldInfo }

		fun build() = MapFile(
			assetList = assetList ?: throwIllegalStateExceptionForField("assetList"),
			buildLists = buildLists ?: throwIllegalStateExceptionForField("buildLists"),
			globalVersion = globalVersion ?: throwIllegalStateExceptionForField("globalVersion"),
			mpPositionList = mpPositionList ?: throwIllegalStateExceptionForField("mpPositionList"),
			worldInfo = worldInfo ?: throwIllegalStateExceptionForField("worldInfo")
		)

		private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
			throw IllegalStateException("Field '$fieldName' is null.")
		}
	}
}
