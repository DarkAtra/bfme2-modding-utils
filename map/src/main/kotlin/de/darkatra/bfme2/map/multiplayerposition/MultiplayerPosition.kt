package de.darkatra.bfme2.map.multiplayerposition

import de.darkatra.bfme2.map.Asset
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "MPPositionInfo", version = 1u)
data class MultiplayerPosition(
    val isHuman: Boolean,
    val isComputer: Boolean,
    val loadAIScript: Boolean,
    val team: UInt,
    val sideRestrictions: List<String>
)
