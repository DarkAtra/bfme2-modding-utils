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
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 1)
    val blendTileData: BlendTileData,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 8)
    val buildLists: BuildLists,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 18)
    val cameraAnimations: CameraAnimations,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 17)
    val cameras: Cameras,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 16)
    val environmentData: EnvironmentData,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 14)
    val globalLighting: GlobalLighting,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE)
    val heightMap: HeightMap,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 5)
    val libraryMapsList: LibraryMapsList,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 3)
    val multiplayerPositions: MultiplayerPositions,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 9)
    val objects: Objects,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 7)
    val playerScriptsList: PlayerScriptsList,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 15)
    val postEffects: PostEffects,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 12)
    val riverAreas: RiverAreas,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 4)
    val sides: Sides,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 11)
    val standingWaterAreas: StandingWaterAreas,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 13)
    val standingWaveAreas: StandingWaveAreas,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 6)
    val teams: Teams,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 10)
    val triggerAreas: TriggerAreas,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 19)
    val waypointList: WaypointList,
    @SerializationOrder(SerializationOrder.HIGHEST_PRECEDENCE + 2)
    val worldInfo: WorldInfo
)
