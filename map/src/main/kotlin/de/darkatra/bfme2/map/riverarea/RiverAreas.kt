package de.darkatra.bfme2.map.riverarea

import de.darkatra.bfme2.map.Asset
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "RiverAreas", version = 2u)
data class RiverAreas(
    val areas: List<RiverArea>
)
