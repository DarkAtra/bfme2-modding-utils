package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class MPPositionInfo(
	val isHuman: Boolean,
	val isComputer: Boolean,
	val loadAIScript: Boolean?,
	val team: Int,
	val sideRestrictions: List<String>
) : Asset {

	companion object : AssetReader<MPPositionInfo> {
		const val ASSET_NAME = "MPPositionInfo"

		override fun read(reader: CountingInputStream, context: MapFileParseContext): MPPositionInfo {

			return AssetReader.readAsset(reader, context) { version ->

				val isHuman = reader.readBoolean()
				val isComputer = reader.readBoolean()

				val loadAIScript: Boolean? = when (version > 0) {
					// I'm guessing about what 5 bytes are missing in version 0, compared to version 1.
					true -> reader.readBoolean()
					else -> null
				}

				val team = reader.readInt()

				val sideRestrictions: MutableList<String> = mutableListOf()
				if (version > 0) {
					val sideRestrictionsLength = reader.readInt()
					for (i in 0 until sideRestrictionsLength) {
						sideRestrictions.add(
							reader.readShortPrefixedString()
						)
					}
				}

				MPPositionInfo(
					isHuman = isHuman,
					isComputer = isComputer,
					loadAIScript = loadAIScript,
					team = team,
					sideRestrictions = sideRestrictions
				)
			}
		}
	}
}
