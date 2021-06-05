package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.AssetName
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

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, AssetName.OBJECTS_LIST.assetName) {

			val objects = mutableListOf<MapObject>()

			MapFileReader.readAssets(reader, context) { assetName ->
				if (assetName != AssetName.OBJECT.assetName) {
					throw InvalidDataException("Unexpected asset name '$assetName' reading ${AssetName.OBJECTS_LIST.assetName}.")
				}

				MapFileReader.readAsset(reader, context, AssetName.OBJECT.assetName) {
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
