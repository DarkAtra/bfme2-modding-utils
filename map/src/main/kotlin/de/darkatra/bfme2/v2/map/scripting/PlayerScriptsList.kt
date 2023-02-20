package de.darkatra.bfme2.v2.map.scripting

import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.deserialization.AssetListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize

@Asset(name = "PlayerScriptsList", version = 1u)
data class PlayerScriptsList(
    val scriptLists: @Deserialize(using = AssetListDeserializer::class) List<ScriptList>
)
