package de.darkatra.bfme2.map.wave

import de.darkatra.bfme2.map.Asset

@Asset(name = "StandingWaveAreas", version = 2u)
data class StandingWaveAreas(
    val areas: List<StandingWaveArea>
)
