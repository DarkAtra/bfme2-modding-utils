package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.MapFileDeserializer

@Deserialize(using = MapFileDeserializer::class)
data class MapFile(
    val blendTileData: BlendTileData,
    val buildLists: BuildLists,
    val cameraAnimations: CameraAnimations,
    val cameras: Cameras,
    val environmentData: EnvironmentData,
    val globalLighting: GlobalLighting,
    val heightMap: HeightMap,
    val libraryMapsList: LibraryMapsList,
    val multiplayerPositions: MultiplayerPositions,
    val objects: Objects,
    val playerScriptsList: PlayerScriptsList,
    val postEffects: PostEffects,
    val riverAreas: RiverAreas,
    val sides: Sides,
    val standingWaterAreas: StandingWaterAreas,
    val standingWaveAreas: StandingWaveAreas,
    val teams: Teams,
    val triggerAreas: TriggerAreas,
    val waypointList: WaypointList,
    val worldInfo: WorldInfo
)
