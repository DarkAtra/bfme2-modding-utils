package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.LibraryMaps
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream


class LibraryMapsListReader : AssetReader {

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

        MapFileReader.readAsset(reader, context, AssetName.LIBRARY_MAP_LISTS.assetName) {

            val libraryMaps = mutableListOf<LibraryMaps>()

            MapFileReader.readAssets(reader, context) { assetName ->
                if (assetName != AssetName.LIBRARY_MAPS.assetName) {
                    throw InvalidDataException("Unexpected asset name '$assetName' reading ${AssetName.LIBRARY_MAP_LISTS.assetName}.")
                }

                MapFileReader.readAsset(reader, context, AssetName.LIBRARY_MAPS.assetName) {

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
