package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.Skybox
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class SkyboxReader : AssetReader {

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, AssetName.POLYGON_TRIGGERS.assetName) {

			val position = Vector3(
				x = reader.readFloat(),
				y = reader.readFloat(),
				z = reader.readFloat()
			)
			val scale = reader.readFloat()
			// radians to degrees
			val rotation = reader.readFloat() * (180 / Math.PI).toFloat()
			val textureScheme = reader.readUShortPrefixedString()

			builder.skybox(Skybox(
				position = position,
				scale = scale,
				rotation = rotation,
				textureScheme = textureScheme
			))
		}
	}
}
