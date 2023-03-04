package de.darkatra.bfme2.map.multiplayerposition

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListDeserializer
import de.darkatra.bfme2.map.serialization.Deserialize

@Asset(name = "MPPositionList", version = 0u)
data class MultiplayerPositions(
    val positions: @Deserialize(using = AssetListDeserializer::class) List<MultiplayerPosition>
)
