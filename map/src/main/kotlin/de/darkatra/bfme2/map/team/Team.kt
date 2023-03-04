package de.darkatra.bfme2.map.team

import de.darkatra.bfme2.map.property.Property
import de.darkatra.bfme2.map.serialization.ListDeserializer

data class Team(
    val properties: @ListDeserializer.Properties(sizeType = ListDeserializer.SizeType.USHORT) List<Property>
)
