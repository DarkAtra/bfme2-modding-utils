package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.property.PropertyKey
import de.darkatra.bfme2.map.serialization.ScriptArgumentSerde
import de.darkatra.bfme2.map.serialization.Serialize
import de.darkatra.bfme2.map.serialization.UIntBooleanSerde

@Asset(name = "ScriptAction", version = 3u)
data class Action(
    val type: ScriptActionType,
    val internalName: PropertyKey,
    val arguments: List<@Serialize(using = ScriptArgumentSerde::class) ScriptArgument>,
    val enabled: @Serialize(using = UIntBooleanSerde::class) Boolean
) : Statement
