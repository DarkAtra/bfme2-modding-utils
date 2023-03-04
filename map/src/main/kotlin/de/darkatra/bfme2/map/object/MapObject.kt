package de.darkatra.bfme2.map.`object`

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.property.Property
import de.darkatra.bfme2.map.serialization.ListDeserializer

@Asset(name = "Object", version = 3u)
data class MapObject(
    val position: Vector3,
    val angle: Float,
    val roadType: RoadType,
    val typeName: String,
    val properties: @ListDeserializer.Properties(sizeType = ListDeserializer.SizeType.USHORT) List<Property>
)
