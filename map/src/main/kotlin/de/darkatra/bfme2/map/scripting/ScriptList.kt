package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.PublicApi
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListSerde
import de.darkatra.bfme2.map.serialization.Serialize
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "ScriptList", version = 1u)
data class ScriptList(
    val scriptListEntries: @Serialize(using = AssetListSerde::class) List<ScriptListEntry>
) {

    @PublicApi
    val scripts = scriptListEntries.filterIsInstance<Script>()

    @PublicApi
    val scriptFolders = scriptListEntries.filterIsInstance<ScriptFolder>()
}
