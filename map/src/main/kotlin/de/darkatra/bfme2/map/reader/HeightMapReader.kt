package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.HeightMap
import de.darkatra.bfme2.map.HeightMapBorder
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import org.apache.commons.io.input.CountingInputStream

class HeightMapReader : AssetReader {

	companion object {
		const val MIN_VERSION_WITH_ELEVATIONS_AS_SHORT = 5u
		const val MIN_VERSION_WITH_HEIGHT_MAP_BORDER_OFFSET = 6u
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, AssetName.HEIGHT_MAP_DATA.assetName) { version ->

			val width = reader.readUInt()
			val height = reader.readUInt()

			val borderWidth = reader.readUInt()

			val numberOfBorders = reader.readUInt()

			val borders = mutableListOf<HeightMapBorder>()
			for (i in 0u until numberOfBorders step 1) {
				borders.add(
					when (version >= MIN_VERSION_WITH_HEIGHT_MAP_BORDER_OFFSET) {
						true -> HeightMapBorder(
							x1 = reader.readUInt(),
							y1 = reader.readUInt(),
							x2 = reader.readUInt(),
							y2 = reader.readUInt()
						)
						false -> HeightMapBorder(
							x2 = reader.readUInt(),
							y2 = reader.readUInt()
						)
					}
				)
			}

			val area = reader.readUInt()
			if (width * height != area) {
				throw InvalidDataException("Width ($width) times height ($height) does not equal to the area ($area).")
			}

			val elevations = mutableMapOf<UInt, MutableMap<UInt, UShort>>()
			for (x in 0u until width step 1) {
				elevations[x] = mutableMapOf()
				for (y in 0u until height step 1) {
					elevations[x]!![y] = when (version >= MIN_VERSION_WITH_ELEVATIONS_AS_SHORT) {
						true -> reader.readUShort()
						false -> reader.readByte().toUShort()
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
				).also { heightMap ->
					context.heightMap = heightMap
				}
			)
		}
	}
}
