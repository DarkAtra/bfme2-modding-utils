package de.darkatra.bfme2.map.multiplayerposition

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListSerde
import de.darkatra.bfme2.map.serialization.Serialize

@Asset(name = "MPPositionList", version = 0u)
data class MultiplayerPositions(
    val positions: @Serialize(using = AssetListSerde::class) List<MultiplayerPosition>
)
