package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer

@Asset(name = "Teams")
data class Teams(
    val teams: List<Team>
) {

    data class Team(
        val properties: @ListDeserializer.Properties(sizeType = ListDeserializer.SizeType.USHORT) List<Property>
    )
}
