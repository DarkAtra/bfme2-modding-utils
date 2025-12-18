package de.darkatra.bfme2.map.property

import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class PropertyKey(
    val propertyType: PropertyType,
    val name: String
)
