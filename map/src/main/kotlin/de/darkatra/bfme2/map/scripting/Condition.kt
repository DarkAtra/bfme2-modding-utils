package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.property.PropertyKey
import de.darkatra.bfme2.map.serialization.ScriptArgumentSerde
import de.darkatra.bfme2.map.serialization.Serialize
import de.darkatra.bfme2.map.serialization.UIntBooleanSerde

@Asset(name = "Condition", version = 6u)
data class Condition(
    val type: ScriptConditionType,
    val internalName: PropertyKey,
    val arguments: List<@Serialize(using = ScriptArgumentSerde::class) ScriptArgument>,
    val enabled: @Serialize(using = UIntBooleanSerde::class) Boolean,
    val inverted: @Serialize(using = UIntBooleanSerde::class) Boolean
) : Statement
