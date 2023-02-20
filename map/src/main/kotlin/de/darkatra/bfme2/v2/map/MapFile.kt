package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.blendtile.BlendTileData
import de.darkatra.bfme2.v2.map.buildlist.BuildLists
import de.darkatra.bfme2.v2.map.camera.CameraAnimations
import de.darkatra.bfme2.v2.map.camera.Cameras
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.MapFileDeserializer
import de.darkatra.bfme2.v2.map.environment.EnvironmentData
import de.darkatra.bfme2.v2.map.globallighting.GlobalLighting
import de.darkatra.bfme2.v2.map.heightmap.HeightMap
import de.darkatra.bfme2.v2.map.librarymap.LibraryMapsList
import de.darkatra.bfme2.v2.map.multiplayerposition.MultiplayerPositions
import de.darkatra.bfme2.v2.map.`object`.Objects
import de.darkatra.bfme2.v2.map.player.Sides
import de.darkatra.bfme2.v2.map.posteffect.PostEffects
import de.darkatra.bfme2.v2.map.riverarea.RiverAreas
import de.darkatra.bfme2.v2.map.scripting.PlayerScriptsList
import de.darkatra.bfme2.v2.map.team.Teams
import de.darkatra.bfme2.v2.map.trigger.TriggerAreas
import de.darkatra.bfme2.v2.map.water.StandingWaterAreas
import de.darkatra.bfme2.v2.map.wave.StandingWaveAreas
import de.darkatra.bfme2.v2.map.waypoint.WaypointList
import de.darkatra.bfme2.v2.map.worldinfo.WorldInfo

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
