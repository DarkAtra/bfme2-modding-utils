package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.Player
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readUInt
import org.apache.commons.io.input.CountingInputStream

class SidesReader(
    private val playerReader: PlayerReader,
    private val playerScriptsReader: PlayerScriptsReader,
    private val teamsReader: TeamsReader
) : AssetReader {

    companion object {
        const val VERSION_WITH_TEAM_AND_SCRIPTS_IN_SEPARATE_CHUNK = 5u
        const val VERSION_WITH_UNKNOWN_FLAG = 6u
    }

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

        MapFileReader.readAsset(reader, context, AssetName.SIDES_LIST.assetName) { version ->

            // TODO: what is this used for? is `false` a reasonable default?
            val unknown = when (version >= VERSION_WITH_UNKNOWN_FLAG) {
                true -> reader.readBoolean()
                else -> false
            }
            builder.unknown(
                unknown = unknown
            )

            val numberOfPlayers = reader.readUInt()

            val players = mutableListOf<Player>()
            for (i in 0u until numberOfPlayers step 1) {
                players.add(
                    playerReader.read(reader, context)
                )
            }
            builder.players(
                players = players
            )

            // version 5 or above has data for teams and scripts in a separate top-level chunk
            if (version >= VERSION_WITH_TEAM_AND_SCRIPTS_IN_SEPARATE_CHUNK) {
                return@readAsset
            }

            // read teams
            teamsReader.read(reader, context, builder)

            var alreadyParsedPlayerScripts = false

            // even though only one script list is allowed per map it is stored inside an array
            MapFileReader.readAssets(reader, context) { assetName ->
                if (assetName != AssetName.PLAYER_SCRIPTS_LIST.assetName) {
                    throw InvalidDataException("Unexpected asset name '$assetName' reading ${AssetName.SIDES_LIST.assetName}.")
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
