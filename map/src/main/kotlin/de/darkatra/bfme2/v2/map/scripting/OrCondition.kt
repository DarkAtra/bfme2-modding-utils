package de.darkatra.bfme2.v2.map.scripting

import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.deserialization.AssetListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize

@Asset(name = "OrCondition", version = 1u)
data class OrCondition(
    val conditions: @Deserialize(using = AssetListDeserializer::class) List<Condition>
) : Statement
