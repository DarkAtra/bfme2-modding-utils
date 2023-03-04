package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.map.Asset

@Asset(name = "ScriptGroup", version = 3u)
data class ScriptFolder(
    override val name: String,
    override val active: Boolean,
    override val subroutine: Boolean,
    val scriptListEntries: List<ScriptListEntry>
) : ScriptListEntry {

    val scripts = scriptListEntries.filterIsInstance<Script>()
    val scriptFolders = scriptListEntries.filterIsInstance<ScriptFolder>()
}
