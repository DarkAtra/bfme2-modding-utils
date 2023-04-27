package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.map.serialization.ConditionalSerde
import de.darkatra.bfme2.map.serialization.Serialize

@Serialize(using = ConditionalSerde::class)
@ConditionalSerde.Properties(assetTypes = [Action::class, ActionFalse::class, Condition::class, OrCondition::class])
sealed interface Statement
