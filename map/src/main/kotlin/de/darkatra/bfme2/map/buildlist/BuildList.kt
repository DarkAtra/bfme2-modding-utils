package de.darkatra.bfme2.map.buildlist

import de.darkatra.bfme2.map.property.PropertyKey
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class BuildList(
    val factionName: PropertyKey,
    val buildListItems: List<BuildListItem>
)
