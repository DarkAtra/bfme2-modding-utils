package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer

@Asset(name = "SidesList", version = 6u)
data class Sides(
    val unknown: Boolean,
    val players: List<Player>
) {

    data class Player(
        val properties: @ListDeserializer.Properties(sizeType = ListDeserializer.SizeType.USHORT) List<Property>,
        val buildListItems: List<BuildListItem>
    )
}
