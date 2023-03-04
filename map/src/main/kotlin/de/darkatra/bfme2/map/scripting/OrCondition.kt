package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListDeserializer
import de.darkatra.bfme2.map.serialization.Deserialize

@Asset(name = "OrCondition", version = 1u)
data class OrCondition(
    val conditions: @Deserialize(using = AssetListDeserializer::class) List<Condition>
) : Statement
