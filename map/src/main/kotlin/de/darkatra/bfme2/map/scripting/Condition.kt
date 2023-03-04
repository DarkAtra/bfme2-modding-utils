package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.property.PropertyKey
import de.darkatra.bfme2.map.serialization.Deserialize
import de.darkatra.bfme2.map.serialization.ScriptArgumentDeserializer
import de.darkatra.bfme2.map.serialization.UIntBooleanDeserializer

@Asset(name = "Condition", version = 6u)
data class Condition(
    val type: ScriptConditionType,
    val internalName: PropertyKey,
    val arguments: List<@Deserialize(using = ScriptArgumentDeserializer::class) ScriptArgument>,
    val enabled: @Deserialize(using = UIntBooleanDeserializer::class) Boolean,
    val inverted: @Deserialize(using = UIntBooleanDeserializer::class) Boolean
) : Statement
