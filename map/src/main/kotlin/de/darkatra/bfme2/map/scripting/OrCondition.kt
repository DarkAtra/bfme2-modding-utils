package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListSerde
import de.darkatra.bfme2.map.serialization.Serialize

@Asset(name = "OrCondition", version = 1u)
data class OrCondition(
    val conditions: @Serialize(using = AssetListSerde::class) List<Condition>
) : Statement
