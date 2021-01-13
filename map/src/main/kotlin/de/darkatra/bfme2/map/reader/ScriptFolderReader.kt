package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.Script
import de.darkatra.bfme2.map.ScriptFolder
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class ScriptFolderReader(
	private val scriptReader: ScriptReader
) {

	companion object {
		const val ASSET_NAME = "ScriptGroup"
		const val MIN_VERSION_WITH_NESTED_SCRIPT_GROUPS = 3
	}

	fun read(reader: CountingInputStream, context: MapFileParseContext): ScriptFolder {

		val scriptFolderBuilder = ScriptFolder.Builder()

		MapFileReader.readAsset(reader, context, ASSET_NAME) { version ->

			val name = reader.readShortPrefixedString()
			val active = reader.readBoolean()
			val subroutine = reader.readBoolean()

			val scripts = mutableListOf<Script>()
			val scriptFolders = mutableListOf<ScriptFolder>()

			MapFileReader.readAssets(reader, context) { assetName ->
				when {
					assetName == ASSET_NAME && version >= MIN_VERSION_WITH_NESTED_SCRIPT_GROUPS.toUShort() -> scriptFolders.add(
						read(reader, context)
					)
					assetName == ScriptReader.ASSET_NAME -> scripts.add(
						scriptReader.read(reader, context)
					)
				}
			}

			scriptFolderBuilder
				.name(name)
				.active(active)
				.subroutine(subroutine)
				.scriptFolders(scriptFolders)
				.scripts(scripts)
		}

		return scriptFolderBuilder.build()
	}
}

