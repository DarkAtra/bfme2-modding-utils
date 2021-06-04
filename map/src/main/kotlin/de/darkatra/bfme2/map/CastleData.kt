package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector3

data class CastleData(
	val propertyKey: PropertyKey,
	val castleTemplates: List<CastleTemplate>,
	val castlePerimeterPoints: List<Vector3>
)
