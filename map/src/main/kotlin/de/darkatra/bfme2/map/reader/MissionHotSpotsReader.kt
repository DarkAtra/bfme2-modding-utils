package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.MissionHotSpot
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class MissionHotSpotsReader : AssetReader {

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, AssetName.MISSION_HOT_SPOTS.assetName) {

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
