package de.darkatra.bfme2.map

data class HeightMap(
	val width: Int,
	val height: Int,
	val borderWidth: Int,
	val borders: List<HeightMapBorder>,
	val elevations: Array<ShortArray>,
	val area: Int = width * height
) {

	// explicit equals and hashCode due to array property (elevations)
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as HeightMap

		if (width != other.width) return false
		if (height != other.height) return false
		if (borderWidth != other.borderWidth) return false
		if (borders != other.borders) return false
		if (!elevations.contentDeepEquals(other.elevations)) return false
		if (area != other.area) return false

		return true
	}

	override fun hashCode(): Int {
		var result = width
		result = 31 * result + height
		result = 31 * result + borderWidth
		result = 31 * result + borders.hashCode()
		result = 31 * result + elevations.contentDeepHashCode()
		result = 31 * result + area
		return result
	}
}
