package de.darkatra.bfme2.map.water

import de.darkatra.bfme2.map.Asset

@Asset(name = "StandingWaterAreas", version = 2u)
data class StandingWaterAreas(
    val areas: List<StandingWaterArea>
)
