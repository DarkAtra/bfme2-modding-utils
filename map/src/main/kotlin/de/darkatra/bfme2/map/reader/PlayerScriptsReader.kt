package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.PlayerScript
import de.darkatra.bfme2.map.Script
import de.darkatra.bfme2.map.ScriptFolder
import org.apache.commons.io.input.CountingInputStream

class PlayerScriptsReader(
    propertyKeyReader: PropertyKeyReader
) : AssetReader {

    private val scriptReader = ScriptReader(propertyKeyReader)
    private val scriptFolderReader = ScriptFolderReader(scriptReader)

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

        val playerScripts = mutableListOf<PlayerScript>()

        MapFileReader.readAsset(reader, context, AssetName.PLAYER_SCRIPTS_LIST.assetName) {

            MapFileReader.readAssets(reader, context) { assetName ->
                if (assetName != AssetName.SCRIPT_LIST.assetName) {
                    throw InvalidDataException("Unexpected asset name '$assetName' reading ${AssetName.SCRIPT_LIST.assetName}.")
                }

                MapFileReader.readAsset(reader, context, AssetName.SCRIPT_LIST.assetName) { version ->

                    if (version != 1.toUShort()) {
                        throw InvalidDataException("Unsupported version of ${AssetName.SCRIPT_LIST.assetName}.")
                    }

                    val scripts = mutableListOf<Script>()
                    val scriptFolders = mutableListOf<ScriptFolder>()

                    MapFileReader.readAssets(reader, context) { assetName ->
                        when (assetName) {
                            AssetName.SCRIPT_GROUP.assetName -> scriptFolders.add(
                                scriptFolderReader.read(reader, context)
                            )

                            AssetName.SCRIPT.assetName -> scripts.add(
                                scriptReader.read(reader, context)
                            )
                        }
                    }

                    playerScripts.add(
                        PlayerScript(
                            scriptFolders = scriptFolders,
                            scripts = scripts
                        )
                    )
                }
            }
        }

        builder.playerScripts(
            playerScripts = playerScripts
        )
    }
}

