package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.BuildList
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readUInt
import org.apache.commons.io.input.CountingInputStream

class BuildListsReader(
	private val buildListReader: BuildListReader
) : AssetReader {

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, AssetName.BUILD_LISTS.assetName) {

			val numberOfBuildLists = reader.readUInt()

			val buildLists = mutableListOf<BuildList>()
			for (i in 0u until numberOfBuildLists step 1) {
				buildLists.add(
					buildListReader.read(reader, context)
				)
			}

			builder.buildLists(
				buildLists = buildLists
			)
		}
	}
}
