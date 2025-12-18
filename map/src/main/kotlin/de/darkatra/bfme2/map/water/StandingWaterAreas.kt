package de.darkatra.bfme2.map.water

import de.darkatra.bfme2.map.Asset
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "StandingWaterAreas", version = 2u)
data class StandingWaterAreas(
    val areas: List<StandingWaterArea>
)
