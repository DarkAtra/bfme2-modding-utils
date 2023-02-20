package de.darkatra.bfme2.v2.map.multiplayerposition

import de.darkatra.bfme2.v2.map.Asset

@Asset(name = "MPPositionInfo", version = 1u)
data class MultiplayerPosition(
    val isHuman: Boolean,
    val isComputer: Boolean,
    val loadAIScript: Boolean,
    val team: UInt,
    val sideRestrictions: List<String>
)
