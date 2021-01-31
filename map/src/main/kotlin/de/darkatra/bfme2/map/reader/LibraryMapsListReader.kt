package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.LibraryMaps
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream


class LibraryMapsListReader : AssetReader {

	companion object {
		const val ASSET_NAME = "LibraryMapLists"
		const val LIBRARY_MAPS_ASSET_NAME = "LibraryMaps"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) {

			val libraryMaps = mutableListOf<LibraryMaps>()

			MapFileReader.readAssets(reader, context) { assetName ->
				if (assetName != LIBRARY_MAPS_ASSET_NAME) {
					throw InvalidDataException("Unexpected asset name '$assetName' reading ${ASSET_NAME}.")
				}

				MapFileReader.readAsset(reader, context, LIBRARY_MAPS_ASSET_NAME) {

					val numberOfLibraryMaps = reader.readUInt()

					val values = mutableListOf<String>()
					for (i in 0u until numberOfLibraryMaps step 1) {
						values.add(
							reader.readUShortPrefixedString()
						)
					}

					libraryMaps.add(
						LibraryMaps(
							values = values
						)
					)
				}
			}

			builder.libraryMaps(libraryMaps)
		}
	}
}
