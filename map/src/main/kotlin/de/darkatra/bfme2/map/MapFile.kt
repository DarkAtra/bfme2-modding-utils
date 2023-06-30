package de.darkatra.bfme2.map

import de.darkatra.bfme2.map.blendtile.BlendTileData
import de.darkatra.bfme2.map.buildlist.BuildLists
import de.darkatra.bfme2.map.camera.CameraAnimations
import de.darkatra.bfme2.map.camera.Cameras
import de.darkatra.bfme2.map.environment.EnvironmentData
import de.darkatra.bfme2.map.globallighting.GlobalLighting
import de.darkatra.bfme2.map.heightmap.HeightMap
import de.darkatra.bfme2.map.librarymap.LibraryMapsList
import de.darkatra.bfme2.map.multiplayerposition.MultiplayerPositions
import de.darkatra.bfme2.map.`object`.Objects
import de.darkatra.bfme2.map.player.Sides
import de.darkatra.bfme2.map.posteffect.PostEffects
import de.darkatra.bfme2.map.riverarea.RiverAreas
import de.darkatra.bfme2.map.scripting.PlayerScriptsList
import de.darkatra.bfme2.map.serialization.MapFileSerde
import de.darkatra.bfme2.map.serialization.SerializationOrder
import de.darkatra.bfme2.map.serialization.Serialize
import de.darkatra.bfme2.map.team.Teams
import de.darkatra.bfme2.map.trigger.TriggerAreas
import de.darkatra.bfme2.map.water.StandingWaterAreas
import de.darkatra.bfme2.map.wave.StandingWaveAreas
import de.darkatra.bfme2.map.waypoint.WaypointList
import de.darkatra.bfme2.map.worldinfo.WorldInfo

@Serialize(using = MapFileSerde::class)
data class MapFile(
    val blendTileData: BlendTileData,
    val buildLists: BuildLists,
    val cameraAnimations: CameraAnimations,
    val cameras: Cameras,
    val environmentData: EnvironmentData,
    val globalLighting: GlobalLighting,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE)
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
