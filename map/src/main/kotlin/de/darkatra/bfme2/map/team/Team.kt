package de.darkatra.bfme2.map.team

import de.darkatra.bfme2.map.property.Property
import de.darkatra.bfme2.map.serialization.ListSerde

data class Team(
    val properties: @ListSerde.Properties(sizeType = ListSerde.SizeType.USHORT) List<Property>
)
