package de.darkatra.bfme2.v2.map.player

import de.darkatra.bfme2.v2.map.buildlist.BuildListItem
import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer
import de.darkatra.bfme2.v2.map.property.Property

data class Player(
    val properties: @ListDeserializer.Properties(sizeType = ListDeserializer.SizeType.USHORT) List<Property>,
    val buildListItems: List<BuildListItem>
)
