package de.darkatra.bfme2.v2.map.scripting

import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.deserialization.AssetListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize

@Asset(name = "ScriptList", version = 1u)
data class ScriptList(
    val scriptListEntries: @Deserialize(using = AssetListDeserializer::class) List<ScriptListEntry>
) {

    val scripts = scriptListEntries.filterIsInstance<Script>()
    val scriptFolders = scriptListEntries.filterIsInstance<ScriptFolder>()
}
