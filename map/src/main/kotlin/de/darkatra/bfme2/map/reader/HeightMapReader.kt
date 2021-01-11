package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.HeightMap
import de.darkatra.bfme2.map.HeightMapBorder
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readShort
import org.apache.commons.io.input.CountingInputStream

class HeightMapReader : AssetReader {

	companion object {
		const val ASSET_NAME = "HeightMapData"
		const val MIN_VERSION_WITH_ELEVATIONS_AS_SHORT = 5
		const val MIN_VERSION_WITH_HEIGHT_MAP_BORDER_OFFSET = 6
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) { version ->

			val width = reader.readInt()
			val height = reader.readInt()

			val borderWidth = reader.readInt()

			val numberOfBorders = reader.readInt()

			val borders = mutableListOf<HeightMapBorder>()
			for (i in 0 until numberOfBorders step 1) {
				borders.add(
					when (version >= MIN_VERSION_WITH_HEIGHT_MAP_BORDER_OFFSET) {
						true -> HeightMapBorder(
							x1 = reader.readInt(),
							y1 = reader.readInt(),
							x2 = reader.readInt(),
							y2 = reader.readInt()
						)
						false -> HeightMapBorder(
							x2 = reader.readInt(),
							y2 = reader.readInt()
						)
					}
				)
			}

			val area = reader.readInt()
			if (width * height != area) {
				throw InvalidDataException("Width ($width) times height ($height) does not equal to the area ($area).")
			}

			val elevations = Array(width) { ShortArray(height) }
			for (x in 0 until width step 1) {
				for (y in 0 until height step 1) {
					elevations[x][y] = when (version >= MIN_VERSION_WITH_ELEVATIONS_AS_SHORT) {
						true -> reader.readShort()
						false -> reader.readByte().toShort()
					}
				}
			}

			builder.heightMap(
				HeightMap(
					width = width,
					height = height,
					borderWidth = borderWidth,
					borders = borders,
					elevations = elevations
				)
			)
		}
	}
}
