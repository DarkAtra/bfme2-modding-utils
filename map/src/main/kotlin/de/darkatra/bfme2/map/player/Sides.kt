package de.darkatra.bfme2.map.player

import de.darkatra.bfme2.map.Asset
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "SidesList", version = 6u)
data class Sides(
    val unknown: Boolean,
    val players: List<Player>
)
