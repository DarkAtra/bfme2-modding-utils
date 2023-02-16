package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.deserialization.AssetListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.UShortPrefixedStringDeserializer

@Asset(name = "MPPositionList")
data class MultiplayerPositions(
    val positions: @Deserialize(using = AssetListDeserializer::class) List<MultiplayerPosition>
) {

    @Asset(name = "MPPositionInfo")
    data class MultiplayerPosition(
        val isHuman: Boolean,
        val isComputer: Boolean,
        val loadAIScript: Boolean,
        val team: UInt,
        val sideRestrictions: List<@Deserialize(using = UShortPrefixedStringDeserializer::class) String>
    )
}
