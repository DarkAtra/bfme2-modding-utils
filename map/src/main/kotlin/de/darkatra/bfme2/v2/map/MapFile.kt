package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.MapFileDeserializer

@Deserialize(using = MapFileDeserializer::class)
data class MapFile(
    val blendTileDataV18: BlendTileDataV18,
    val heightMapV5: HeightMapV5,
    val multiplayerPositions: MultiplayerPositions,
    val sides: Sides,
    val worldInfo: WorldInfo
)
