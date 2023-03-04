package de.darkatra.bfme2.map.player

import de.darkatra.bfme2.map.buildlist.BuildListItem
import de.darkatra.bfme2.map.property.Property
import de.darkatra.bfme2.map.serialization.ListDeserializer

data class Player(
    val properties: @ListDeserializer.Properties(sizeType = ListDeserializer.SizeType.USHORT) List<Property>,
    val buildListItems: List<BuildListItem>
)
