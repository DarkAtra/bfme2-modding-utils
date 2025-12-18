package de.darkatra.bfme2.map.trigger

import de.darkatra.bfme2.Vector2
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class TriggerArea(
    val name: String,
    val layerName: String,
    val id: UInt,
    val points: List<Vector2>,
    val unknown: UInt
)
