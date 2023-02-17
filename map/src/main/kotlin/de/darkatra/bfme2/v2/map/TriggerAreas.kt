package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.Vector2

@Asset(name = "TriggerAreas", version = 1u)
data class TriggerAreas(
    val areas: List<TriggerArea>
) {

    data class TriggerArea(
        val name: String,
        val layerName: String,
        val id: UInt,
        val points: List<Vector2>,
        val unknown: UInt
    )
}
