package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.MapFileDeserializer

@Deserialize(using = MapFileDeserializer::class)
data class MapFile(
    val blendTileData: BlendTileData,
    val heightMap: HeightMap,
    val libraryMapsList: LibraryMapsList,
    val multiplayerPositions: MultiplayerPositions,
    val sides: Sides,
    val teams: Teams,
    val worldInfo: WorldInfo
)
