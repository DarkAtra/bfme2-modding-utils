package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.BuildList
import de.darkatra.bfme2.map.BuildListItem
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.map.MapFileReader
import de.darkatra.bfme2.map.PropertyKey
import de.darkatra.bfme2.map.PropertyType
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readShortPrefixedString
import de.darkatra.bfme2.toLittleEndianInt
import org.apache.commons.io.input.CountingInputStream

class BuildListsReader : AssetReader {

	companion object {
		const val ASSET_NAME = "BuildLists"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) {

			val numberOfBuildLists = reader.readInt()

			val buildLists = mutableListOf<BuildList>()
			for (i in 0 until numberOfBuildLists step 1) {
				buildLists.add(
					readBuildList(reader, context)
				)
			}

			builder.buildLists(
				buildLists = buildLists
			)
		}
	}

	private fun readBuildList(reader: CountingInputStream, context: MapFileParseContext): BuildList {

		// hacky way to detect if the map is using the c&c map format
		val factionName = when (context.mapHasAssetList) {
			true -> reader.readShortPrefixedString()
			false -> readPropertyKey(reader, context).name
		}

		val numberOfBuildListItems = reader.readInt()

		val buildListItems = mutableListOf<BuildListItem>()
		for (i in 0 until numberOfBuildListItems step 1) {
			buildListItems.add(
				readBuildListItem(reader, context)
			)
		}

		return BuildList(
			factionName = factionName,
			buildListItems = buildListItems
		)
	}

	private fun readBuildListItem(reader: CountingInputStream, context: MapFileParseContext): BuildListItem {

		val buildingName = reader.readShortPrefixedString()
		val name = reader.readShortPrefixedString()
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

		val rebuilds = reader.readInt()
		val script = reader.readShortPrefixedString()
		val startingHealth = reader.readInt()

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

	private fun readPropertyKey(reader: CountingInputStream, context: MapFileParseContext): PropertyKey {

		val propertyType = PropertyType.ofByte(reader.readByte())

		val nameIndex = reader.readNBytes(3).toLittleEndianInt()
		val name = context.getAssetName(nameIndex)

		return PropertyKey(
			propertyType = propertyType,
			name = name
		)
	}
}
