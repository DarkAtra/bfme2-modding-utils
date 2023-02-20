package de.darkatra.bfme2.v2.map.player

import de.darkatra.bfme2.v2.map.Asset

@Asset(name = "SidesList", version = 6u)
data class Sides(
    val unknown: Boolean,
    val players: List<Player>
)
