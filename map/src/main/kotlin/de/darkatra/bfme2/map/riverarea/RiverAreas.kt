package de.darkatra.bfme2.map.riverarea

import de.darkatra.bfme2.map.Asset

@Asset(name = "RiverAreas", version = 2u)
data class RiverAreas(
    val areas: List<RiverArea>
)
