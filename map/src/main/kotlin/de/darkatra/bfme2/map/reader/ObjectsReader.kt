package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.MapObject
import de.darkatra.bfme2.map.RoadType
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream


class ObjectsReader(
	private val propertiesReader: PropertiesReader
) : AssetReader {

	companion object {
		const val ASSET_NAME = "ObjectsList"
		const val MAP_OBJECT_ASSET_NAME = "Object"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) {

			val objects = mutableListOf<MapObject>()

			MapFileReader.readAssets(reader, context) { assetName ->
				if (assetName != MAP_OBJECT_ASSET_NAME) {
					throw InvalidDataException("Unexpected asset name '$assetName' reading ${ASSET_NAME}.")
				}

				MapFileReader.readAsset(reader, context, MAP_OBJECT_ASSET_NAME) {
					objects.add(
						MapObject(
							position = Vector3(
								reader.readFloat(),
								reader.readFloat(),
								reader.readFloat()
							),
							angle = reader.readFloat(),
							roadType = RoadType.ofUInt(reader.readUInt()),
							typeName = reader.readUShortPrefixedString(),
							properties = propertiesReader.read(reader, context)
						)
					)
				}
			}

			builder.objects(objects)
		}
	}
}
