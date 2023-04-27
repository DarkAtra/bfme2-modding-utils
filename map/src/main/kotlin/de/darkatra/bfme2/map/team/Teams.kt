package de.darkatra.bfme2.map.team

import de.darkatra.bfme2.map.Asset

@Asset(name = "Teams", version = 1u)
data class Teams(
    val teams: List<Team>
)
