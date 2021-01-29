package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.BlendTileTexture
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class BlendTileTextureReader {

	fun read(reader: CountingInputStream): BlendTileTexture {

		val cellStart = reader.readUInt()
		val cellCount = reader.readUInt()
		val cellSize = reader.readUInt()

		if (cellSize * cellSize != cellCount) {
			throw InvalidDataException("Expected cell count to equal cell size times cell size.")
		}

		val magicValue = reader.readUInt()
		if (magicValue != 0u) {
			throw InvalidDataException("Magic value was not 0.")
		}

		val name = reader.readUShortPrefixedString()

		return BlendTileTexture(
			name = name,
			cellStart = cellStart,
			cellCount = cellCount,
			cellSize = cellSize,
			magicValue = magicValue
		)
	}
}
