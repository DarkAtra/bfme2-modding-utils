package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.MapFileDeserializer

@Deserialize(using = MapFileDeserializer::class)
data class MapFile(
    val blendTileData: BlendTileData,
    val buildLists: BuildLists,
    val globalLighting: GlobalLighting,
    val heightMap: HeightMap,
    val libraryMapsList: LibraryMapsList,
    val multiplayerPositions: MultiplayerPositions,
    val objects: Objects,
    val playerScriptsList: PlayerScriptsList,
    val riverAreas: RiverAreas,
    val sides: Sides,
    val standingWaterAreas: StandingWaterAreas,
    val standingWaveAreas: StandingWaveAreas,
    val teams: Teams,
    val triggerAreas: TriggerAreas,
    val worldInfo: WorldInfo
)
