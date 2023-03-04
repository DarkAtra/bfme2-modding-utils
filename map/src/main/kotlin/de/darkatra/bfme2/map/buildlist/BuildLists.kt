package de.darkatra.bfme2.map.buildlist

import de.darkatra.bfme2.map.Asset

@Asset(name = "BuildLists", version = 1u)
data class BuildLists(
    val buildLists: List<BuildList>
)
