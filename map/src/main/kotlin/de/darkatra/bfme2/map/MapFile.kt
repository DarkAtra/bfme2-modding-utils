package de.darkatra.bfme2.map

/**
 * Represents one .map file, but is not tightly coupled to the serialization format for a .map file.
 * This makes it easier to work with the data, as it does not depend on the [version][globalVersion] of the serialization format.
 *
 * Can be serialized and deserialized via [MapFileWriter][de.darkatra.bfme2.map.writer.MapFileWriter] and [MapFileReader][de.darkatra.bfme2.map.reader.MapFileReader]
 */
data class MapFile(
	// TODO: add version fields
	val assetList: List<AssetListItem>?,
	val blendTileData: BlendTileData,
	val buildLists: List<BuildList>?,
	val cameraAnimations: List<CameraAnimation>?,
	val cameras: List<Camera>?,
	val castleData: CastleData?,
	val environmentData: EnvironmentData?,
	val fogSettings: FogSettings?,
	val globalLighting: GlobalLighting,
	val globalVersion: UShort?,
	val globalWaterSettings: GlobalWaterSettings?,
	val heightMap: HeightMap,
	val libraryMaps: List<LibraryMaps>?,
	val missionHotSpots: List<MissionHotSpot>?,
	val missionObjectives: List<MissionObjective>?,
	val multiplayerPositions: List<MultiplayerPosition>?,
	val objects: List<MapObject>,
	val players: List<Player>,
	val playerScripts: List<PlayerScript>?,
	val polygonTriggers: List<PolygonTrigger>?,
	val postEffects: List<PostEffect>?,
	val riverAreas: List<RiverArea>?,
	val skybox: Skybox?,
	val standingWaterAreas: List<StandingWaterArea>?,
	val standingWaveAreas: List<StandingWaveArea>?,
	val teams: List<Team>?,
	val triggerAreas: List<TriggerArea>?,
	val unknown: Boolean,
	val waypointPaths: List<WaypointPath>,
	val worldSettings: List<Property>
) {

	class Builder {
		// TODO: change back to private once the writer is implemented, only internal for testing purposes
		internal var assetList: List<AssetListItem>? = null
		internal var blendTileData: BlendTileData? = null
		internal var buildLists: List<BuildList>? = null
		internal var cameraAnimations: List<CameraAnimation>? = null
		internal var cameras: List<Camera>? = null
		internal var castleData: CastleData? = null
		internal var environmentData: EnvironmentData? = null
		internal var fogSettings: FogSettings? = null
		internal var globalLighting: GlobalLighting? = null
		internal var globalVersion: UShort? = null
		internal var globalWaterSettings: GlobalWaterSettings? = null
		internal var heightMap: HeightMap? = null
		internal var libraryMaps: List<LibraryMaps>? = null
		internal var missionHotSpots: List<MissionHotSpot>? = null
		internal var missionObjectives: List<MissionObjective>? = null
		internal var multiplayerPositions: List<MultiplayerPosition>? = null
		internal var objects: List<MapObject>? = null
		internal var players: List<Player>? = null
		internal var playerScripts: List<PlayerScript>? = null
		internal var polygonTriggers: List<PolygonTrigger>? = null
		internal var postEffects: List<PostEffect>? = null
		internal var riverAreas: List<RiverArea>? = null
		internal var skybox: Skybox? = null
		internal var standingWaterAreas: List<StandingWaterArea>? = null
		internal var standingWaveAreas: List<StandingWaveArea>? = null
		internal var teams: List<Team>? = null
		internal var triggerAreas: List<TriggerArea>? = null
		internal var unknown: Boolean? = null
		internal var waypointPaths: List<WaypointPath>? = null
		internal var worldSettings: List<Property>? = null

		constructor()

		constructor(mapFile: MapFile) {
			assetList(mapFile.assetList)
			assetList(mapFile.assetList)
			blendTileData(mapFile.blendTileData)
			buildLists(mapFile.buildLists)
			cameraAnimations(mapFile.cameraAnimations)
			cameras(mapFile.cameras)
			castleData(mapFile.castleData)
			environmentData(mapFile.environmentData)
			fogSettings(mapFile.fogSettings)
			globalLighting(mapFile.globalLighting)
			globalVersion(mapFile.globalVersion)
			globalWaterSettings(mapFile.globalWaterSettings)
			heightMap(mapFile.heightMap)
			libraryMaps(mapFile.libraryMaps)
			missionHotSpots(mapFile.missionHotSpots)
			missionObjectives(mapFile.missionObjectives)
			multiplayerPositions(mapFile.multiplayerPositions)
			objects(mapFile.objects)
			players(mapFile.players)
			playerScripts(mapFile.playerScripts)
			polygonTriggers(mapFile.polygonTriggers)
			postEffects(mapFile.postEffects)
			riverAreas(mapFile.riverAreas)
			skybox(mapFile.skybox)
			standingWaterAreas(mapFile.standingWaterAreas)
			standingWaveAreas(mapFile.standingWaveAreas)
			teams(mapFile.teams)
			triggerAreas(mapFile.triggerAreas)
			unknown(mapFile.unknown)
			waypointPaths(mapFile.waypointPaths)
			worldSettings(mapFile.worldSettings)
		}

		fun assetList(assetList: List<AssetListItem>?) = apply { this.assetList = assetList }
		fun blendTileData(blendTileData: BlendTileData) = apply { this.blendTileData = blendTileData }
		fun buildLists(buildLists: List<BuildList>?) = apply { this.buildLists = buildLists }
		fun cameraAnimations(cameraAnimations: List<CameraAnimation>?) = apply { this.cameraAnimations = cameraAnimations }
		fun cameras(cameras: List<Camera>?) = apply { this.cameras = cameras }
		fun castleData(castleData: CastleData?) = apply { this.castleData = castleData }
		fun environmentData(environmentData: EnvironmentData?) = apply { this.environmentData = environmentData }
		fun fogSettings(fogSettings: FogSettings?) = apply { this.fogSettings = fogSettings }
		fun globalLighting(globalLighting: GlobalLighting) = apply { this.globalLighting = globalLighting }
		fun globalVersion(globalVersion: UShort?) = apply { this.globalVersion = globalVersion }
		fun globalWaterSettings(globalWaterSettings: GlobalWaterSettings?) = apply { this.globalWaterSettings = globalWaterSettings }
		fun heightMap(heightMap: HeightMap) = apply { this.heightMap = heightMap }
		fun libraryMaps(libraryMaps: List<LibraryMaps>?) = apply { this.libraryMaps = libraryMaps }
		fun missionHotSpots(missionHotSpots: List<MissionHotSpot>?) = apply { this.missionHotSpots = missionHotSpots }
		fun missionObjectives(missionObjectives: List<MissionObjective>?) = apply { this.missionObjectives = missionObjectives }
		fun multiplayerPositions(multiplayerPositions: List<MultiplayerPosition>?) = apply { this.multiplayerPositions = multiplayerPositions }
		fun objects(objects: List<MapObject>) = apply { this.objects = objects }
		fun players(players: List<Player>) = apply { this.players = players }
		fun playerScripts(playerScripts: List<PlayerScript>?) = apply { this.playerScripts = playerScripts }
		fun polygonTriggers(polygonTriggers: List<PolygonTrigger>?) = apply { this.polygonTriggers = polygonTriggers }
		fun postEffects(postEffects: List<PostEffect>?) = apply { this.postEffects = postEffects }
		fun riverAreas(riverAreas: List<RiverArea>?) = apply { this.riverAreas = riverAreas }
		fun skybox(skybox: Skybox?) = apply { this.skybox = skybox }
		fun standingWaterAreas(standingWaterAreas: List<StandingWaterArea>?) = apply { this.standingWaterAreas = standingWaterAreas }
		fun standingWaveAreas(standingWaveAreas: List<StandingWaveArea>?) = apply { this.standingWaveAreas = standingWaveAreas }
		fun teams(teams: List<Team>?) = apply { this.teams = teams }
		fun triggerAreas(triggerAreas: List<TriggerArea>?) = apply { this.triggerAreas = triggerAreas }
		fun unknown(unknown: Boolean) = apply { this.unknown = unknown }
		fun waypointPaths(waypointPaths: List<WaypointPath>) = apply { this.waypointPaths = waypointPaths }
		fun worldSettings(worldSettings: List<Property>) = apply { this.worldSettings = worldSettings }

		fun build() = MapFile(
			assetList = assetList,
			blendTileData = blendTileData ?: throwIllegalStateExceptionForField("blendTileData"),
			buildLists = buildLists,
			cameraAnimations = cameraAnimations,
			cameras = cameras,
			castleData = castleData,
			environmentData = environmentData,
			fogSettings = fogSettings,
			globalLighting = globalLighting ?: throwIllegalStateExceptionForField("globalLighting"),
			globalVersion = globalVersion,
			globalWaterSettings = globalWaterSettings,
			heightMap = heightMap ?: throwIllegalStateExceptionForField("heightMap"),
			libraryMaps = libraryMaps,
			missionHotSpots = missionHotSpots,
			missionObjectives = missionObjectives,
			multiplayerPositions = multiplayerPositions,
			objects = objects ?: throwIllegalStateExceptionForField("objects"),
			players = players ?: throwIllegalStateExceptionForField("players"),
			playerScripts = playerScripts,
			polygonTriggers = polygonTriggers,
			postEffects = postEffects,
			riverAreas = riverAreas,
			skybox = skybox,
			standingWaterAreas = standingWaterAreas,
			standingWaveAreas = standingWaveAreas,
			teams = teams,
			triggerAreas = triggerAreas,
			unknown = unknown ?: throwIllegalStateExceptionForField("unknown"),
			waypointPaths = waypointPaths ?: throwIllegalStateExceptionForField("waypointPaths"),
			worldSettings = worldSettings ?: throwIllegalStateExceptionForField("worldSettings")
		)

		private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
			throw IllegalStateException("Field '$fieldName' is null.")
		}
	}
}
