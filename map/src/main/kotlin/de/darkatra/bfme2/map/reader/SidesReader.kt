package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.map.MapFileReader
import de.darkatra.bfme2.map.Player
import de.darkatra.bfme2.map.PlayerScript
import de.darkatra.bfme2.map.Team
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readInt
import org.apache.commons.io.input.CountingInputStream

class SidesReader : AssetReader {

	companion object {
		const val ASSET_NAME = "SidesList"
		const val PLAYER_SCRIPTS_LIST_ASSET_NAME = "PlayerScriptsList"
		const val SCRIPTS_LIST_ASSET_NAME = "ScriptList"
		const val SCRIPT_FOLDER_ASSET_NAME = "ScriptGroup"
		const val SCRIPT_ASSET_NAME = "Script"
		const val VERSION_WITH_TEAM_AND_SCRIPTS_IN_SEPARATE_CHUNK = 5
		const val VERSION_WITH_UNKNOWN_FLAG = 6
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, ASSET_NAME) { version ->

			// TODO: what is this? is `false` a reasonable default?
			val unknown = when (version >= VERSION_WITH_UNKNOWN_FLAG) {
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
					Player() // TODO: read Player
				)
			}
			builder.players(
				players = players
			)

			// version 5 or above has data for teams and scripts in a separate top-level chunk
			if (version >= VERSION_WITH_TEAM_AND_SCRIPTS_IN_SEPARATE_CHUNK) {
				return@readAsset
			}

			val numberOfTeams = reader.readInt()

			val teams = mutableListOf<Team>()
			for (i in 0 until numberOfTeams step 1) {
				teams.add(
					Team() // TODO: read Team
				)
			}
			builder.teams(
				teams = teams
			)

			val playerScripts = mutableListOf<PlayerScript>()

			// TODO: this should probably be moved to another class
			// even though only one script list is allowed per map it is stored inside of an array
			MapFileReader.readAssets(reader, context) { assetName ->
				if (assetName != PLAYER_SCRIPTS_LIST_ASSET_NAME) {
					throw InvalidDataException("Unexpected asset name '$assetName' reading $ASSET_NAME.")
				}

				// abort if there are more than one script lists
				if (playerScripts.isNotEmpty()) {
					throw InvalidDataException("Found more than one script list.")
				}

				// TODO: this should probably be moved to another class
				MapFileReader.readAsset(reader, context, PLAYER_SCRIPTS_LIST_ASSET_NAME) {
					if (assetName != SCRIPTS_LIST_ASSET_NAME) {
						throw InvalidDataException("Unexpected asset name '$assetName' reading $PLAYER_SCRIPTS_LIST_ASSET_NAME.")
					}

					MapFileReader.readAssets(reader, context) { assetName ->
						if (assetName !== SCRIPTS_LIST_ASSET_NAME) {
							throw InvalidDataException("Unexpected asset name '$assetName' reading $SCRIPTS_LIST_ASSET_NAME.")
						}

						// TODO: this should probably be moved to another class
						MapFileReader.readAsset(reader, context, SCRIPTS_LIST_ASSET_NAME) { version ->

							if (version != 1.toShort()) {
								throw InvalidDataException("Unsupported version of $SCRIPTS_LIST_ASSET_NAME.")
							}

							MapFileReader.readAssets(reader, context) { assetName ->
								when (assetName) {
									SCRIPT_FOLDER_ASSET_NAME -> {
										// TODO: read Folders containing more even folders or scripts
									}
									SCRIPT_ASSET_NAME -> {
										// TODO: read scripts
									}
								}
							}
						}
					}
				}

				// TODO: set resulting scripts in builder
			}
		}
	}
}
