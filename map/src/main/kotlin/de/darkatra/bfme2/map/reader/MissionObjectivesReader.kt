package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.MissionObjective
import de.darkatra.bfme2.map.MissionObjectiveType
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class MissionObjectivesReader : AssetReader {

	companion object {
		const val ASSET_NAME = "MissionObjectives"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) {

			val numberOfMissionObjectives = reader.readUInt()

			val missionObjectives = mutableListOf<MissionObjective>()
			for (i in 0u until numberOfMissionObjectives step 1) {
				missionObjectives.add(
					MissionObjective(
						id = reader.readUShortPrefixedString(),
						title = reader.readUShortPrefixedString(),
						description = reader.readUShortPrefixedString(),
						isBonusObjective = reader.readBoolean(),
						objectiveType = MissionObjectiveType.ofUInt(reader.readUInt())
					)
				)
			}

			builder.missionObjectives(missionObjectives)
		}

	}
}
