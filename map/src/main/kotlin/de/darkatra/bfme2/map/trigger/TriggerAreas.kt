package de.darkatra.bfme2.map.trigger

import de.darkatra.bfme2.map.Asset
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "TriggerAreas", version = 1u)
data class TriggerAreas(
    val areas: List<TriggerArea>
)
