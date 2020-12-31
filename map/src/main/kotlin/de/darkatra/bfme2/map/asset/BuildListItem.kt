package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readShortPrefixedString
import de.darkatra.bfme2.toBoolean
import de.darkatra.bfme2.toLittleEndianFloat
import de.darkatra.bfme2.toLittleEndianInt
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

	companion object : AssetReader<BuildListItem>() {

		override fun read(reader: CountingInputStream, context: MapFileParseContext): BuildListItem {

			val buildingName = reader.readShortPrefixedString()
			val name = reader.readShortPrefixedString()
			val position = Vector3(
				x = reader.readNBytes(4).toLittleEndianFloat(),
				y = reader.readNBytes(4).toLittleEndianFloat(),
				z = reader.readNBytes(4).toLittleEndianFloat()
			)
			val angle = reader.readNBytes(4).toLittleEndianFloat()
			val isAlreadyBuilt = reader.readByte().toBoolean()

			// hacky way to detect if the map is using the c&c map format
			val unknown1 = when (context.mapHasAssetList) {
				true -> reader.readByte().toBoolean()
				false -> null
			}

			val rebuilds = reader.readNBytes(4).toLittleEndianInt()
			val script = reader.readShortPrefixedString()
			val startingHealth = reader.readNBytes(4).toLittleEndianInt()

			// one of these unknown booleans reflects the "Unsellable" checkbox in Building Properties
			val unknown2 = reader.readByte().toBoolean()
			val unknown3 = reader.readByte().toBoolean()
			val unknown4 = reader.readByte().toBoolean()

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
