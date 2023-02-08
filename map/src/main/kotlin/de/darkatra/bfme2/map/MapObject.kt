package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector3

data class MapObject(
    val position: Vector3,
    val angle: Float,
    val roadType: RoadType,
    val typeName: String,
    val properties: List<Property>
)
