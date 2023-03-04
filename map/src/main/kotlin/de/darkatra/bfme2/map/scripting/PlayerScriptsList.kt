package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListDeserializer
import de.darkatra.bfme2.map.serialization.Deserialize

@Asset(name = "PlayerScriptsList", version = 1u)
data class PlayerScriptsList(
    val scriptLists: @Deserialize(using = AssetListDeserializer::class) List<ScriptList>
)
