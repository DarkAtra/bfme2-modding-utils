package de.darkatra.bfme2.map

/**
 * Represents one .map file, but is not tightly coupled to the serialization format for a .map file.
 * This makes it easier to work with the data, as it does not depend on the [version][globalVersion] of the serialization format.
 *
 * Can be serialized and deserialized via [MapFileWriter][de.darkatra.bfme2.map.writer.MapFileWriter] and [MapFileReader][de.darkatra.bfme2.map.reader.MapFileReader]
 */
data class MapFile(
	// TODO: add version fields
	val assetList: List<AssetListItem>,
	val blendTileData: BlendTileData,
	val buildLists: List<BuildList>,
	val fogSettings: FogSettings,
	val globalVersion: UShort,
	val globalWaterSettings: GlobalWaterSettings,
	val heightMap: HeightMap,
	val libraryMaps: List<LibraryMaps>,
	val missionHotSpots: List<MissionHotSpot>,
	val missionObjectives: List<MissionObjective>,
	val multiplayerPositions: List<MultiplayerPosition>,
	val objects: List<MapObject>,
	val players: List<Player>,
	val playerScripts: List<PlayerScript>,
	val polygonTriggers: List<PolygonTrigger>,
	val standingWaterAreas: List<StandingWaterArea>,
	val teams: List<Team>,
	val triggerAreas: List<TriggerArea>,
	val unknown: Boolean,
	val worldSettings: List<Property>
) {

	class Builder {
		private var assetList: List<AssetListItem>? = null
		private var blendTileData: BlendTileData? = null
		private var buildLists: List<BuildList>? = null
		private var fogSettings: FogSettings? = null
		private var globalVersion: UShort? = null
		private var globalWaterSettings: GlobalWaterSettings? = null
		private var heightMap: HeightMap? = null
		private var libraryMaps: List<LibraryMaps>? = null
		private var missionHotSpots: List<MissionHotSpot>? = null
		private var missionObjectives: List<MissionObjective>? = null
		private var multiplayerPositions: List<MultiplayerPosition>? = null
		private var objects: List<MapObject>? = null
		private var players: List<Player>? = null
		private var playerScripts: List<PlayerScript>? = null
		private var polygonTriggers: List<PolygonTrigger>? = null
		private var standingWaterAreas: List<StandingWaterArea>? = null
		private var teams: List<Team>? = null
		private var triggerAreas: List<TriggerArea>? = null
		private var unknown: Boolean? = null
		private var worldSettings: List<Property>? = null

		fun assetList(assetList: List<AssetListItem>) = apply { this.assetList = assetList }
		fun blendTileData(blendTileData: BlendTileData) = apply { this.blendTileData = blendTileData }
		fun buildLists(buildLists: List<BuildList>) = apply { this.buildLists = buildLists }
		fun fogSettings(fogSettings: FogSettings) = apply { this.fogSettings = fogSettings }
		fun globalVersion(globalVersion: UShort) = apply { this.globalVersion = globalVersion }
		fun globalWaterSettings(globalWaterSettings: GlobalWaterSettings) = apply { this.globalWaterSettings = globalWaterSettings }
		fun heightMap(heightMap: HeightMap) = apply { this.heightMap = heightMap }
		fun libraryMaps(libraryMaps: List<LibraryMaps>) = apply { this.libraryMaps = libraryMaps }
		fun missionHotSpots(missionHotSpots: List<MissionHotSpot>) = apply { this.missionHotSpots = missionHotSpots }
		fun missionObjectives(missionObjectives: List<MissionObjective>) = apply { this.missionObjectives = missionObjectives }
		fun multiplayerPositions(multiplayerPositions: List<MultiplayerPosition>) = apply { this.multiplayerPositions = multiplayerPositions }
		fun objects(objects: List<MapObject>) = apply { this.objects = objects }
		fun players(players: List<Player>) = apply { this.players = players }
		fun playerScripts(playerScripts: List<PlayerScript>) = apply { this.playerScripts = playerScripts }
		fun polygonTriggers(polygonTriggers: List<PolygonTrigger>) = apply { this.polygonTriggers = polygonTriggers }
		fun standingWaterAreas(standingWaterAreas: List<StandingWaterArea>) = apply { this.standingWaterAreas = standingWaterAreas }
		fun teams(teams: List<Team>) = apply { this.teams = teams }
		fun triggerAreas(triggerAreas: List<TriggerArea>) = apply { this.triggerAreas = triggerAreas }
		fun unknown(unknown: Boolean) = apply { this.unknown = unknown }
		fun worldSettings(worldSettings: List<Property>) = apply { this.worldSettings = worldSettings }

		fun build() = MapFile(
			assetList = assetList ?: throwIllegalStateExceptionForField("assetList"),
			blendTileData = blendTileData ?: throwIllegalStateExceptionForField("blendTileData"),
			buildLists = buildLists ?: throwIllegalStateExceptionForField("buildLists"),
			fogSettings = fogSettings ?: throwIllegalStateExceptionForField("fogSettings"),
			globalVersion = globalVersion ?: throwIllegalStateExceptionForField("globalVersion"),
			globalWaterSettings = globalWaterSettings ?: throwIllegalStateExceptionForField("globalWaterSettings"),
			heightMap = heightMap ?: throwIllegalStateExceptionForField("heightMap"),
			libraryMaps = libraryMaps ?: throwIllegalStateExceptionForField("libraryMaps"),
			missionHotSpots = missionHotSpots ?: throwIllegalStateExceptionForField("missionHotSpots"),
			missionObjectives = missionObjectives ?: throwIllegalStateExceptionForField("missionObjectives"),
			multiplayerPositions = multiplayerPositions ?: throwIllegalStateExceptionForField("multiplayerPositions"),
			objects = objects ?: throwIllegalStateExceptionForField("objects"),
			players = players ?: throwIllegalStateExceptionForField("players"),
			playerScripts = playerScripts ?: throwIllegalStateExceptionForField("playerScripts"),
			polygonTriggers = polygonTriggers ?: throwIllegalStateExceptionForField("polygonTriggers"),
			standingWaterAreas = standingWaterAreas ?: throwIllegalStateExceptionForField("standingWaterAreas"),
			teams = teams ?: throwIllegalStateExceptionForField("teams"),
			triggerAreas = triggerAreas ?: throwIllegalStateExceptionForField("triggerAreas"),
			unknown = unknown ?: throwIllegalStateExceptionForField("unknown"),
			worldSettings = worldSettings ?: throwIllegalStateExceptionForField("worldSettings")
		)

		private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
			throw IllegalStateException("Field '$fieldName' is null.")
		}
	}
}
