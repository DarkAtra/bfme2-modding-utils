package de.darkatra.bfme2.map.worldinfo

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.property.Property
import de.darkatra.bfme2.map.serialization.ListSerde
import de.darkatra.bfme2.map.serialization.ListSerde.SizeType

@Asset(name = "WorldInfo", version = 1u)
data class WorldInfo(
    val properties: @ListSerde.Properties(sizeType = SizeType.USHORT) List<Property>
) {

    operator fun get(propertyName: String): Property? {
        return properties.firstOrNull { property ->
            property.key.name == propertyName
        }
    }
}
