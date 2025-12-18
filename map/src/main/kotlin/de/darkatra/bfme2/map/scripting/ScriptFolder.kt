package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.PublicApi
import de.darkatra.bfme2.map.Asset
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "ScriptGroup", version = 3u)
data class ScriptFolder(
    override val name: String,
    override val active: Boolean,
    override val subroutine: Boolean,
    val scriptListEntries: List<ScriptListEntry>
) : ScriptListEntry {

    @PublicApi
    val scripts = scriptListEntries.filterIsInstance<Script>()

    @PublicApi
    val scriptFolders = scriptListEntries.filterIsInstance<ScriptFolder>()
}
