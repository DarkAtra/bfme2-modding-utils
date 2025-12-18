package de.darkatra.bfme2.map.team

import de.darkatra.bfme2.map.property.Property
import de.darkatra.bfme2.map.serialization.ListSerde
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class Team(
    val properties: @ListSerde.Properties(sizeType = ListSerde.SizeType.USHORT) List<Property>
)
