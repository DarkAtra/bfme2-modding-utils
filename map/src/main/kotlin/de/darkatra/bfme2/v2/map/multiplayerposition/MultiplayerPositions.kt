package de.darkatra.bfme2.v2.map.multiplayerposition

import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.deserialization.AssetListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize

@Asset(name = "MPPositionList", version = 0u)
data class MultiplayerPositions(
    val positions: @Deserialize(using = AssetListDeserializer::class) List<MultiplayerPosition>
)
