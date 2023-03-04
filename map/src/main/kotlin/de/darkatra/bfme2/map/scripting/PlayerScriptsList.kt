package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListSerde
import de.darkatra.bfme2.map.serialization.Serialize

@Asset(name = "PlayerScriptsList", version = 1u)
data class PlayerScriptsList(
    val scriptLists: @Serialize(using = AssetListSerde::class) List<ScriptList>
)
