package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.BlendTileTexture
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class BlendTileTextureReader {

	fun read(reader: CountingInputStream): BlendTileTexture {

		val cellStart = reader.readInt()
		val cellCount = reader.readInt()
		val cellSize = reader.readInt()

		if (cellSize * cellSize != cellCount) {
			throw InvalidDataException("Expected cell count to equal cell size times cell size.")
		}

		val magicValue = reader.readInt()
		if (magicValue != 0) {
			throw InvalidDataException("Magic value was not 0.")
		}

		val name = reader.readShortPrefixedString()

		return BlendTileTexture(
			name = name,
			cellStart = cellStart,
			cellCount = cellCount,
			cellSize = cellSize,
			magicValue = magicValue
		)
	}
}
