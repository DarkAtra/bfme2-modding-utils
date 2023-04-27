package de.darkatra.bfme2.map.buildlist

import de.darkatra.bfme2.map.property.PropertyKey

data class BuildList(
    val factionName: PropertyKey,
    val buildListItems: List<BuildListItem>
)
