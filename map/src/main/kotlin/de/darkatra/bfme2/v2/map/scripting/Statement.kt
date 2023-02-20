package de.darkatra.bfme2.v2.map.scripting

import de.darkatra.bfme2.v2.map.deserialization.ConditionalDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize

@Deserialize(using = ConditionalDeserializer::class)
@ConditionalDeserializer.Properties(assetTypes = [Action::class, ActionFalse::class, Condition::class, OrCondition::class])
sealed interface Statement
