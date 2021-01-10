package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.MultiplayerPosition
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class MultiplayerPositionsReader : AssetReader {

	companion object {
		const val ASSET_NAME = "MPPositionList"
		const val MULTIPLAYER_POSITION_ASSET_NAME = "MPPositionInfo"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) {

			val multiplayerPositions = mutableListOf<MultiplayerPosition>()

			MapFileReader.readAssets(reader, context) { assetName ->
				if (assetName !== MULTIPLAYER_POSITION_ASSET_NAME) {
					throw InvalidDataException("Unexpected asset name '$assetName' reading $ASSET_NAME.")
				}

				MapFileReader.readAsset(reader, context, MULTIPLAYER_POSITION_ASSET_NAME) { version ->

					val isHuman = reader.readBoolean()
					val isComputer = reader.readBoolean()

					val loadAIScript = when (version > 0) {
						true -> reader.readBoolean()
						else -> null
					}

					val team = reader.readInt()

					val sideRestrictions = mutableListOf<String>()
					if (version > 0) {
						val sideRestrictionsLength = reader.readInt()
						for (i in 0 until sideRestrictionsLength step 1) {
							sideRestrictions.add(
								reader.readShortPrefixedString()
							)
						}
					}

					multiplayerPositions.add(
						MultiplayerPosition(
							isHuman = isHuman,
							isComputer = isComputer,
							loadAIScript = loadAIScript,
							team = team,
							sideRestrictions = sideRestrictions
						)
					)
				}
			}

			builder.multiplayerPositions(
				multiplayerPositions = multiplayerPositions
			)
		}
	}
}
