package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.MapFileParseContext
import org.apache.commons.io.input.CountingInputStream

class MPPositionList(
	val mpPositions: List<MPPositionInfo>
) : Asset {

	companion object : AssetReader<MPPositionList> {
		const val ASSET_NAME = "MPPositionList"

		override fun read(reader: CountingInputStream, context: MapFileParseContext): MPPositionList {

			return AssetReader.readAsset(reader, context) {

				val mpPositions: MutableList<MPPositionInfo> = mutableListOf()

				AssetReader.readAssets(reader, context) { assetName ->
					if (assetName !== MPPositionInfo.ASSET_NAME) {
						throw InvalidDataException("Unexpected asset name '$assetName' reading $ASSET_NAME.")
					}

					mpPositions.add(
						MPPositionInfo.read(reader, context)
					)
				}

				MPPositionList(
					mpPositions = mpPositions
				)
			}
		}
	}
}
