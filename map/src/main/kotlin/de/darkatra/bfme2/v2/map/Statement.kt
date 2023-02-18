package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.Property.PropertyKey
import de.darkatra.bfme2.v2.map.deserialization.AssetListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.ConditionalDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.ScriptArgumentDeserializer
import de.darkatra.bfme2.v2.map.deserialization.UIntBooleanDeserializer

@Deserialize(using = ConditionalDeserializer::class)
@ConditionalDeserializer.Properties(assetTypes = [Action::class, ActionFalse::class, Condition::class, OrCondition::class])
sealed interface Statement

@Asset(name = "ScriptAction", version = 3u)
data class Action(
    val type: ScriptActionType,
    val internalName: PropertyKey,
    val arguments: List<@Deserialize(using = ScriptArgumentDeserializer::class) ScriptArgument>,
    val enabled: @Deserialize(using = UIntBooleanDeserializer::class) Boolean
) : Statement

@Asset(name = "ScriptActionFalse", version = 3u)
data class ActionFalse(
    val type: ScriptActionType,
    val internalName: PropertyKey,
    val arguments: List<@Deserialize(using = ScriptArgumentDeserializer::class) ScriptArgument>,
    val enabled: @Deserialize(using = UIntBooleanDeserializer::class) Boolean
) : Statement

@Asset(name = "Condition", version = 6u)
data class Condition(
    val type: ScriptConditionType,
    val internalName: PropertyKey,
    val arguments: List<@Deserialize(using = ScriptArgumentDeserializer::class) ScriptArgument>,
    val enabled: @Deserialize(using = UIntBooleanDeserializer::class) Boolean,
    val inverted: @Deserialize(using = UIntBooleanDeserializer::class) Boolean
) : Statement

@Asset(name = "OrCondition", version = 1u)
data class OrCondition(
    val conditions: @Deserialize(using = AssetListDeserializer::class) List<Condition>
) : Statement
