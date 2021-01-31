package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.MissionHotSpot
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class MissionHotSpotsReader : AssetReader {

	companion object {
		const val ASSET_NAME = "MissionHotSpots"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) {

			val numberOfMissionHotSpots = reader.readUInt()

			val missionHotSpots = mutableListOf<MissionHotSpot>()
			for (i in 0u until numberOfMissionHotSpots step 1) {
				missionHotSpots.add(
					MissionHotSpot(
						id = reader.readUShortPrefixedString(),
						title = reader.readUShortPrefixedString(),
						description = reader.readUShortPrefixedString()
					)
				)
			}

			builder.missionHotSpots(missionHotSpots)
		}

	}
}
