package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector2

data class TriggerArea(
    val name: String,
    val layerName: String,
    val id: UInt,
    val points: List<Vector2>,
    val unknown: UInt
)
