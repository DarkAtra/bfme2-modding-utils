package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Vector2
import de.darkatra.bfme2.map.CliffTextureMapping
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readShort
import org.apache.commons.io.input.CountingInputStream

class CliffTextureMappingReader {

	fun read(reader: CountingInputStream): CliffTextureMapping {

		val textureTile = reader.readInt()

		val bottomLeftCoords = Vector2(
			x = reader.readFloat(),
			y = reader.readFloat()
		)
		val bottomRightCoords = Vector2(
			x = reader.readFloat(),
			y = reader.readFloat()
		)
		val topRightCoords = Vector2(
			x = reader.readFloat(),
			y = reader.readFloat()
		)
		val topLeftCoords = Vector2(
			x = reader.readFloat(),
			y = reader.readFloat()
		)

		val unknown2 = reader.readShort()

		return CliffTextureMapping(
			textureTile = textureTile,
			bottomLeftCoords = bottomLeftCoords,
			bottomRightCoords = bottomRightCoords,
			topRightCoords = topRightCoords,
			topLeftCoords = topLeftCoords,
			unknown2 = unknown2
		)
	}
}
