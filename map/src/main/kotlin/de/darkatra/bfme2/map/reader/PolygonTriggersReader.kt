package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.Point3D
import de.darkatra.bfme2.Vector2
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.PolygonTrigger
import de.darkatra.bfme2.map.PolygonTriggerType
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class PolygonTriggersReader : AssetReader {

	companion object {
		const val ASSET_NAME = "PolygonTriggers"
		const val MIN_VERSION_WITH_LAYER_NAME = 4u
		const val MIN_VERSION_WITH_RIVER_SPECIFIC_TEXTURES = 5u
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) { version ->

			val numberOfPolygonTriggers = reader.readUInt()

			val polygonTriggers = mutableListOf<PolygonTrigger>()
			for (i in 0u until numberOfPolygonTriggers step 1) {
				polygonTriggers.add(
					readPolygonTrigger(reader, context, version)
				)
			}

			builder.polygonTriggers(polygonTriggers)
		}
	}

	private fun readPolygonTrigger(reader: CountingInputStream, context: MapFileParseContext, version: UShort): PolygonTrigger {

		val polygonTriggerBuilder = PolygonTrigger.Builder()

		polygonTriggerBuilder.name(reader.readUShortPrefixedString())

		if (version >= MIN_VERSION_WITH_LAYER_NAME) {
			polygonTriggerBuilder.layerName(reader.readUShortPrefixedString())
		}

		polygonTriggerBuilder.id(reader.readUInt())
		polygonTriggerBuilder.triggerType(PolygonTriggerType.ofUShort(reader.readUShort()))

		polygonTriggerBuilder.riverStartControlPoint(reader.readUInt())

		if (version >= MIN_VERSION_WITH_RIVER_SPECIFIC_TEXTURES) {
			polygonTriggerBuilder.riverTexture(reader.readUShortPrefixedString())
			polygonTriggerBuilder.noiseTexture(reader.readUShortPrefixedString())
			polygonTriggerBuilder.alphaEdgeTexture(reader.readUShortPrefixedString())
			polygonTriggerBuilder.sparkleTexture(reader.readUShortPrefixedString())
			polygonTriggerBuilder.bumpMapTexture(reader.readUShortPrefixedString())
			polygonTriggerBuilder.skyTexture(reader.readUShortPrefixedString())
			polygonTriggerBuilder.useAdditiveBlending(reader.readBoolean())
			polygonTriggerBuilder.riverColor(
				Color(
					r = reader.readByte().toInt(),
					g = reader.readByte().toInt(),
					b = reader.readByte().toInt()
				)
			)

			val unknown = reader.readByte()
			if (unknown != 0.toByte()) {
				throw InvalidDataException("Expected unknown to equal '0' but was '$unknown'.")
			}
			polygonTriggerBuilder.unknown(unknown)

			polygonTriggerBuilder.uvScrollSpeed(
				Vector2(
					x = reader.readFloat(),
					y = reader.readFloat()
				)
			)

			polygonTriggerBuilder.riverAlpha(reader.readFloat())
		}

		val numberOfPoints = reader.readUInt()

		val points = mutableListOf<Point3D>()
		for (i in 0u until numberOfPoints step 1) {
			points.add(
				Point3D(
					x = reader.readInt(),
					y = reader.readInt(),
					z = reader.readInt()
				)
			)
		}
		polygonTriggerBuilder.points(points)

		return polygonTriggerBuilder.build()
	}
}
