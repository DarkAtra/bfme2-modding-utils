package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.BuildList
import de.darkatra.bfme2.map.BuildListItem
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class BuildListReader(
	private val propertyKeyReader: PropertyKeyReader
) {

	fun read(reader: CountingInputStream, context: MapFileParseContext): BuildList {

		// hacky way to detect if the map is using the c&c map format
		val factionName = when (context.mapHasAssetList) {
			true -> reader.readUShortPrefixedString()
			false -> propertyKeyReader.read(reader, context).name
		}

		val buildListItems = readBuildListItems(reader, context)

		return BuildList(
			factionName = factionName,
			buildListItems = buildListItems
		)
	}

	fun readBuildListItems(reader: CountingInputStream, context: MapFileParseContext): List<BuildListItem> {

		val numberOfBuildListItems = reader.readUInt()

		val buildListItems = mutableListOf<BuildListItem>()
		for (i in 0u until numberOfBuildListItems step 1) {
			buildListItems.add(
				readBuildListItem(reader, context)
			)
		}

		return buildListItems
	}

	private fun readBuildListItem(reader: CountingInputStream, context: MapFileParseContext): BuildListItem {

		val buildingName = reader.readUShortPrefixedString()
		val name = reader.readUShortPrefixedString()
		val position = Vector3(
			x = reader.readFloat(),
			y = reader.readFloat(),
			z = reader.readFloat()
		)
		val angle = reader.readFloat()
		val isAlreadyBuilt = reader.readBoolean()

		// hacky way to detect if the map is using the c&c map format
		val unknown1 = when (context.mapHasAssetList) {
			true -> reader.readBoolean()
			false -> null
		}

		val rebuilds = reader.readUInt()
		val script = reader.readUShortPrefixedString()
		val startingHealth = reader.readUInt()

		// one of these unknown booleans reflects the "Unsellable" checkbox in Building Properties
		val unknown2 = reader.readBoolean()
		val unknown3 = reader.readBoolean()
		val unknown4 = reader.readBoolean()

		return BuildListItem(
			buildingName = buildingName,
			name = name,
			position = position,
			angle = angle,
			isAlreadyBuilt = isAlreadyBuilt,
			unknown1 = unknown1,
			rebuilds = rebuilds,
			script = script,
			startingHealth = startingHealth,
			unknown2 = unknown2,
			unknown3 = unknown3,
			unknown4 = unknown4
		)
	}
}
