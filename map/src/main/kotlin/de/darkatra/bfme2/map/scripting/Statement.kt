package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.map.serialization.ConditionalDeserializer
import de.darkatra.bfme2.map.serialization.Deserialize

@Deserialize(using = ConditionalDeserializer::class)
@ConditionalDeserializer.Properties(assetTypes = [Action::class, ActionFalse::class, Condition::class, OrCondition::class])
sealed interface Statement
