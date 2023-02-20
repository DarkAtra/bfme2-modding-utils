package de.darkatra.bfme2.v2.map.trigger

import de.darkatra.bfme2.Vector2

data class TriggerArea(
    val name: String,
    val layerName: String,
    val id: UInt,
    val points: List<Vector2>,
    val unknown: UInt
)
