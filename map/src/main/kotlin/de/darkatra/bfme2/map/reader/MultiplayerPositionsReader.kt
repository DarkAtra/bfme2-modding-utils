package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.MultiplayerPosition
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class MultiplayerPositionsReader : AssetReader {

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, AssetName.MP_POSITION_LIST.assetName) {

			val multiplayerPositions = mutableListOf<MultiplayerPosition>()

			MapFileReader.readAssets(reader, context) { assetName ->
				if (assetName != AssetName.MP_POSITION_INFO.assetName) {
					throw InvalidDataException("Unexpected asset name '$assetName' reading ${AssetName.MP_POSITION_LIST.assetName}.")
				}

				MapFileReader.readAsset(reader, context, AssetName.MP_POSITION_INFO.assetName) { version ->

					val isHuman = reader.readBoolean()
					val isComputer = reader.readBoolean()

					val loadAIScript = when (version > 0u) {
						true -> reader.readBoolean()
						else -> null
					}

					val team = reader.readUInt()

					val sideRestrictions = mutableListOf<String>()
					if (version > 0u) {
						val sideRestrictionsLength = reader.readUInt()
						for (i in 0u until sideRestrictionsLength step 1) {
							sideRestrictions.add(
								reader.readUShortPrefixedString()
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
