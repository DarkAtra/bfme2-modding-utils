package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Vector2
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.StandingWaterArea
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class StandingWaterAreasReader : AssetReader {

	companion object {
		const val ASSET_NAME = "StandingWaterAreas"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) {

			val numberOfStandingWaterAreas = reader.readUInt()

			val standingWaterAreas = mutableListOf<StandingWaterArea>()
			for (i in 0u until numberOfStandingWaterAreas step 1) {
				standingWaterAreas.add(
					readStandingWaterArea(reader)
				)
			}

			builder.standingWaterAreas(standingWaterAreas)
		}

	}

	@Suppress("DuplicatedCode")
	private fun readStandingWaterArea(reader: CountingInputStream): StandingWaterArea {

		val id = reader.readUInt()
		val name = reader.readUShortPrefixedString()
		val layerName = reader.readUShortPrefixedString()
		val uvScrollSpeed = reader.readFloat()
		val useAdditiveBlending = reader.readBoolean()
		val bumpMapTexture = reader.readUShortPrefixedString()
		val skyTexture = reader.readUShortPrefixedString()

		val numberOfPoints = reader.readUInt()

		val points = mutableListOf<Vector2>()
		for (i in 0u until numberOfPoints step 1) {
			points.add(
				Vector2(
					x = reader.readFloat(),
					y = reader.readFloat()
				)
			)
		}

		val waterHeight = reader.readUInt()
		val fxShader = reader.readUShortPrefixedString()
		val depthColors = reader.readUShortPrefixedString()

		return StandingWaterArea(
			id = id,
			name = name,
			layerName = layerName,
			uvScrollSpeed = uvScrollSpeed,
			useAdditiveBlending = useAdditiveBlending,
			bumpMapTexture = bumpMapTexture,
			skyTexture = skyTexture,
			point = points,
			waterHeight = waterHeight,
			fxShader = fxShader,
			depthColor = depthColors
		)
	}
}
