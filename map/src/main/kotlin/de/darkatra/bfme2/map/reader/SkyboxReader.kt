package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.Skybox
import de.darkatra.bfme2.readFloat
import org.apache.commons.io.input.CountingInputStream
import java.nio.charset.StandardCharsets

class SkyboxReader : AssetReader {

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, AssetName.POLYGON_TRIGGERS.assetName) {

			val position = Vector3(
				x = reader.readFloat(),
				y = reader.readFloat(),
				z = reader.readFloat()
			)
			val scale = reader.readFloat()
			val rotation = reader.readFloat()
			val textureScheme = reader.readNBytes(12).toString(StandardCharsets.UTF_8)

			builder.skybox(Skybox(
				position = position,
				scale = scale,
				rotation = rotation,
				textureScheme = textureScheme
			))
		}
	}
}
