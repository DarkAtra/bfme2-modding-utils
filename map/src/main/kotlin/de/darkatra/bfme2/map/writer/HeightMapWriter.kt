package de.darkatra.bfme2.map.writer

import de.darkatra.bfme2.getByteCount
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.HeightMap
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.writeByte
import de.darkatra.bfme2.writeUInt
import de.darkatra.bfme2.writeUShort
import org.apache.commons.io.output.CountingOutputStream

class HeightMapWriter : AssetWriter {

	// TODO: currently duplicated from HeightMapReader
	companion object {
		const val MIN_VERSION_WITH_ELEVATIONS_AS_SHORT = 5u
		const val MIN_VERSION_WITH_HEIGHT_MAP_BORDER_OFFSET = 6u
	}

	override fun write(writer: CountingOutputStream, context: MapFileComposeContext, mapFile: MapFile) {

		// TODO: currently hardcoded
		val version = 5u.toUShort()

		writer.writeUInt(context.getOrCreateAssetIndex(AssetName.HEIGHT_MAP_DATA.assetName))

		val heightMap = mapFile.heightMap
		val assetSize = determineAssetSize(heightMap, version)

		MapFileWriter.writeAsset(writer, version, assetSize) {

			writer.writeUInt(heightMap.width)
			writer.writeUInt(heightMap.height)

			writer.writeUInt(heightMap.borderWidth)

			writer.writeUInt(heightMap.borders.size.toUInt())
			heightMap.borders.forEach { border ->
				if (version >= MIN_VERSION_WITH_HEIGHT_MAP_BORDER_OFFSET) {
					writer.writeUInt(border.x1)
					writer.writeUInt(border.y1)
				}
				writer.writeUInt(border.x2)
				writer.writeUInt(border.y2)
			}

			writer.writeUInt(heightMap.area)

			heightMap.elevations.values.forEach { elevations ->
				elevations.values.forEach { elevation ->
					when (version >= MIN_VERSION_WITH_ELEVATIONS_AS_SHORT) {
						true -> writer.writeUShort(elevation)
						false -> writer.writeByte(elevation.toByte())
					}
				}
			}
		}
	}

	override fun composeAssetNames(context: MapFileComposeContext, mapFile: MapFile) {
		context.getOrCreateAssetIndex(AssetName.HEIGHT_MAP_DATA.assetName)
	}

	private fun determineAssetSize(heightMap: HeightMap, version: UShort): UInt {
		return listOf(
			heightMap.width.getByteCount(),
			heightMap.height.getByteCount(),
			heightMap.borderWidth.getByteCount(),
			heightMap.borders.size.toUInt().getByteCount(),
			heightMap.borders.sumOf { border ->
				when (version >= MIN_VERSION_WITH_HEIGHT_MAP_BORDER_OFFSET) {
					true -> border.x1.getByteCount() + border.x2.getByteCount() + border.y1.getByteCount() + border.y2.getByteCount()
					false -> border.x2.getByteCount() + border.y2.getByteCount()
				}
			},
			heightMap.area.getByteCount(),
			heightMap.elevations.values.flatMap { it.values }.sumOf { elevation ->
				when (version >= MIN_VERSION_WITH_ELEVATIONS_AS_SHORT) {
					true -> elevation.getByteCount()
					false -> 1u // 1 byte
				}
			}
		).sum()
	}
}
