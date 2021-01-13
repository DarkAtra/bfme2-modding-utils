package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.Player
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readInt
import org.apache.commons.io.input.CountingInputStream

class SidesReader(
	private val playerReader: PlayerReader,
	private val playerScriptsReader: PlayerScriptsReader,
	private val teamsReader: TeamsReader
) : AssetReader {

	companion object {
		const val ASSET_NAME = "SidesList"
		const val VERSION_WITH_TEAM_AND_SCRIPTS_IN_SEPARATE_CHUNK = 5
		const val VERSION_WITH_UNKNOWN_FLAG = 6
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) { version ->

			// TODO: what is this? is `false` a reasonable default?
			val unknown = when (version >= VERSION_WITH_UNKNOWN_FLAG.toUShort()) {
				true -> reader.readBoolean()
				else -> false
			}
			builder.unknown(
				unknown = unknown
			)

			val numberOfPlayers = reader.readInt()

			val players = mutableListOf<Player>()
			for (i in 0 until numberOfPlayers step 1) {
				players.add(
					playerReader.read(reader, context)
				)
			}
			builder.players(
				players = players
			)

			// version 5 or above has data for teams and scripts in a separate top-level chunk
			if (version >= VERSION_WITH_TEAM_AND_SCRIPTS_IN_SEPARATE_CHUNK.toUShort()) {
				return@readAsset
			}

			// read teams
			teamsReader.read(reader, context, builder)

			var alreadyParsedPlayerScripts = false

			// even though only one script list is allowed per map it is stored inside of an array
			MapFileReader.readAssets(reader, context) { assetName ->
				if (assetName != PlayerScriptsReader.ASSET_NAME) {
					throw InvalidDataException("Unexpected asset name '$assetName' reading $ASSET_NAME.")
				}

				// abort if there are more than one script lists
				if (alreadyParsedPlayerScripts) {
					throw InvalidDataException("Found more than one script list.")
				}

				playerScriptsReader.read(reader, context, builder)

				alreadyParsedPlayerScripts = true
			}
		}
	}
}
