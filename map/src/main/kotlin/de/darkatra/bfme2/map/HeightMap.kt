package de.darkatra.bfme2.map

data class HeightMap(
	val width: UInt,
	val height: UInt,
	val borderWidth: UInt,
	val borders: List<HeightMapBorder>,
	val elevations: Map<UInt, Map<UInt, UShort>>,
	val area: UInt = width * height
)
