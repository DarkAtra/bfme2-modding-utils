package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.UShortPrefixedStringDeserializer

@Asset(name = "SidesList")
data class Sides(
    val unknown: Boolean,
    val players: List<Player>
) {

    data class Player(
        val properties: @ListDeserializer.Properties(sizeType = ListDeserializer.SizeType.USHORT) List<Property>,
        val buildListItems: List<BuildListItem>
    ) {

        data class BuildListItem(
            val buildingName: @Deserialize(using = UShortPrefixedStringDeserializer::class) String,
            val name: @Deserialize(using = UShortPrefixedStringDeserializer::class) String,
            val position: Vector3,
            val angle: Float,
            val isAlreadyBuilt: Boolean,
            val rebuilds: UInt,
            val script: @Deserialize(using = UShortPrefixedStringDeserializer::class) String,
            val startingHealth: UInt,
            val unknown1: Boolean,
            val unknown2: Boolean,
            val unknown3: Boolean,
        )
    }
}
