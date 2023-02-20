package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.toLittleEndianUInt
import de.darkatra.bfme2.v2.map.property.PropertyKey
import de.darkatra.bfme2.v2.map.property.PropertyType
import org.apache.commons.io.input.CountingInputStream

internal class PropertyKeyDeserializer(
    deserializerFactory: DeserializerFactory,
    private val deserializationContext: DeserializationContext,
) : Deserializer<PropertyKey> {

    private val propertyTypeDeserializer: Deserializer<PropertyType> =
        deserializerFactory.getDeserializer(PropertyType::class)

    override fun deserialize(inputStream: CountingInputStream): PropertyKey {

        val propertyType = propertyTypeDeserializer.deserialize(inputStream)

        val assetNameIndex = byteArrayOf(*inputStream.readNBytes(3), 0).toLittleEndianUInt()
        val assetName = deserializationContext.getAssetName(assetNameIndex)

        return PropertyKey(
            propertyType = propertyType,
            name = assetName
        )
    }
}
