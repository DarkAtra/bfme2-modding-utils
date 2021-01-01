package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class BuildList(
	val factionName: String,
	val buildListItems: List<BuildListItem>
) {

	companion object : AssetReader<BuildList> {

		override fun read(reader: CountingInputStream, context: MapFileParseContext): BuildList {

			// hacky way to detect if the map is using the c&c map format
			val factionName = when (context.mapHasAssetList) {
				true -> reader.readShortPrefixedString()
				false -> AssetPropertyKey.read(reader, context).name
			}

			val numberOfBuildListItems = reader.readInt()

			val buildListItems = mutableListOf<BuildListItem>()
			for (i in 0 until numberOfBuildListItems step 1) {
				buildListItems.add(
					BuildListItem.read(reader, context)
				)
			}

			return BuildList(
				factionName = factionName,
				buildListItems = buildListItems
			)
		}
	}
}
