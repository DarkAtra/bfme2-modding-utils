package de.darkatra.bfme2.map.wave

import de.darkatra.bfme2.map.Asset
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "StandingWaveAreas", version = 2u)
data class StandingWaveAreas(
    val areas: List<StandingWaveArea>
)
