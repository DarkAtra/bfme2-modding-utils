package de.darkatra.bfme2.v2.map

@Asset(name = "BuildLists", version = 1u)
data class BuildLists(
    val buildLists: List<BuildList>
) {

    data class BuildList(
        val factionName: Property.PropertyKey,
        val buildListItems: List<BuildListItem>
    )
}
