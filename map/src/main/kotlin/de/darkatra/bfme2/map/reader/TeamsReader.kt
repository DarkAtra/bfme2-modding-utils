package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.Team
import de.darkatra.bfme2.readUInt
import org.apache.commons.io.input.CountingInputStream

class TeamsReader(
	private val propertiesReader: PropertiesReader
) : AssetReader {

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, AssetName.TEAMS.assetName) {

			val numberOfTeams = reader.readUInt()

			val teams = mutableListOf<Team>()
			for (i in 0u until numberOfTeams step 1) {
				teams.add(
					Team(
						properties = propertiesReader.read(reader, context)
					)
				)
			}

			builder.teams(
				teams = teams
			)
		}
	}
}
