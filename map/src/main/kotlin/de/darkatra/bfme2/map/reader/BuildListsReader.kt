package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.BuildList
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readInt
import org.apache.commons.io.input.CountingInputStream

class BuildListsReader(
	private val buildListReader: BuildListReader
) : AssetReader {

	companion object {
		const val ASSET_NAME = "BuildLists"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) {

			val numberOfBuildLists = reader.readInt()

			val buildLists = mutableListOf<BuildList>()
			for (i in 0 until numberOfBuildLists step 1) {
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
