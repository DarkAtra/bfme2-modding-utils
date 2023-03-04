package de.darkatra.bfme2.map.trigger

import de.darkatra.bfme2.map.Asset

@Asset(name = "TriggerAreas", version = 1u)
data class TriggerAreas(
    val areas: List<TriggerArea>
)
