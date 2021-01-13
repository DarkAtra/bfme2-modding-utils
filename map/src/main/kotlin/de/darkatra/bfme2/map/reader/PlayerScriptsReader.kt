package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.PlayerScript
import de.darkatra.bfme2.map.Script
import de.darkatra.bfme2.map.ScriptFolder
import org.apache.commons.io.input.CountingInputStream

class PlayerScriptsReader : AssetReader {

	private val scriptReader = ScriptReader()
	private val scriptFolderReader = ScriptFolderReader(scriptReader)

	companion object {
		const val ASSET_NAME = "PlayerScriptsList"
		const val SCRIPTS_LIST_ASSET_NAME = "ScriptList"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		val playerScripts = mutableListOf<PlayerScript>()

		MapFileReader.readAsset(reader, context, ASSET_NAME) {

			MapFileReader.readAssets(reader, context) { assetName ->
				if (assetName != SCRIPTS_LIST_ASSET_NAME) {
					throw InvalidDataException("Unexpected asset name '$assetName' reading ${SCRIPTS_LIST_ASSET_NAME}.")
				}

				MapFileReader.readAsset(reader, context, SCRIPTS_LIST_ASSET_NAME) { version ->

					if (version != 1.toUShort()) {
						throw InvalidDataException("Unsupported version of ${SCRIPTS_LIST_ASSET_NAME}.")
					}

					val scripts = mutableListOf<Script>()
					val scriptFolders = mutableListOf<ScriptFolder>()

					MapFileReader.readAssets(reader, context) { assetName ->
						when (assetName) {
							ScriptFolderReader.ASSET_NAME -> scriptFolders.add(
								scriptFolderReader.read(reader, context)
							)
							ScriptReader.ASSET_NAME -> scripts.add(
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

