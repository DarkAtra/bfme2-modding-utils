package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.Team
import de.darkatra.bfme2.readInt
import org.apache.commons.io.input.CountingInputStream

class TeamsReader(
	private val propertiesReader: PropertiesReader
) : AssetReader {

	companion object {
		const val ASSET_NAME = "Teams"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) {

			val numberOfTeams = reader.readInt()

			val teams = mutableListOf<Team>()
			for (i in 0 until numberOfTeams step 1) {
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
