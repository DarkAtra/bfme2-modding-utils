package de.darkatra.bfme2.v2.map.team

import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer
import de.darkatra.bfme2.v2.map.property.Property

data class Team(
    val properties: @ListDeserializer.Properties(sizeType = ListDeserializer.SizeType.USHORT) List<Property>
)
