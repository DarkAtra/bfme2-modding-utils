package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.Script
import de.darkatra.bfme2.map.ScriptFolder
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class ScriptFolderReader(
    private val scriptReader: ScriptReader
) {

    companion object {
        const val MIN_VERSION_WITH_NESTED_SCRIPT_GROUPS = 3u
    }

    fun read(reader: CountingInputStream, context: MapFileParseContext): ScriptFolder {

        val scriptFolderBuilder = ScriptFolder.Builder()

        MapFileReader.readAsset(reader, context, AssetName.SCRIPT_GROUP.assetName) { version ->

            val name = reader.readUShortPrefixedString()
            val active = reader.readBoolean()
            val subroutine = reader.readBoolean()

            val scripts = mutableListOf<Script>()
            val scriptFolders = mutableListOf<ScriptFolder>()

            MapFileReader.readAssets(reader, context) { assetName ->
                when {
                    assetName == AssetName.SCRIPT_GROUP.assetName && version >= MIN_VERSION_WITH_NESTED_SCRIPT_GROUPS -> scriptFolders.add(
                        read(reader, context)
                    )

                    assetName == AssetName.SCRIPT.assetName -> scripts.add(
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

