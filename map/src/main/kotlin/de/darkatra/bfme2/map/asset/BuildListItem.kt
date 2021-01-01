package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class BuildListItem(
	val buildingName: String,
	val name: String,
	val position: Vector3,
	val angle: Float,
	val isAlreadyBuilt: Boolean,
	val unknown1: Boolean?,
	val rebuilds: Int,
	val script: String,
	val startingHealth: Int,
	val unknown2: Boolean,
	val unknown3: Boolean,
	val unknown4: Boolean,
) {

	companion object : AssetReader<BuildListItem> {

		override fun read(reader: CountingInputStream, context: MapFileParseContext): BuildListItem {

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
	}
}
