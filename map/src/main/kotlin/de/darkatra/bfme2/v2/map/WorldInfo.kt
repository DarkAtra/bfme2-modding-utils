package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer.SizeType

@Asset(name = "WorldInfo")
data class WorldInfo(
    val properties:
    @ListDeserializer.Properties(sizeType = SizeType.USHORT)
    List<@Deserialize(using = Property.PropertyDeserializer::class) Property>
)
