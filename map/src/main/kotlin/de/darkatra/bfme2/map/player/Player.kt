package de.darkatra.bfme2.map.player

import de.darkatra.bfme2.map.buildlist.BuildListItem
import de.darkatra.bfme2.map.property.Property
import de.darkatra.bfme2.map.serialization.ListSerde
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class Player(
    val properties: @ListSerde.Properties(sizeType = ListSerde.SizeType.USHORT) List<Property>,
    val buildListItems: List<BuildListItem>
)
