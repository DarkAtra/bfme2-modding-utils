package de.darkatra.bfme2.v2.map.worldinfo

import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer.SizeType
import de.darkatra.bfme2.v2.map.property.Property

@Asset(name = "WorldInfo", version = 1u)
data class WorldInfo(
    val properties: @ListDeserializer.Properties(sizeType = SizeType.USHORT) List<Property>
)
