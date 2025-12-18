package de.darkatra.bfme2.map.buildlist

import de.darkatra.bfme2.map.Asset
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "BuildLists", version = 1u)
data class BuildLists(
    val buildLists: List<BuildList>
)
