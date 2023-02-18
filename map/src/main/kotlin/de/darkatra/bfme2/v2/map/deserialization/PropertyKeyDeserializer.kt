package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.toLittleEndianUInt
import de.darkatra.bfme2.v2.map.Property
import org.apache.commons.io.input.CountingInputStream

internal class PropertyKeyDeserializer(
    deserializerFactory: DeserializerFactory,
    private val deserializationContext: DeserializationContext,
) : Deserializer<Property.PropertyKey> {

    private val propertyTypeDeserializer: Deserializer<Property.PropertyKey.PropertyType> =
        deserializerFactory.getDeserializer(Property.PropertyKey.PropertyType::class)

    override fun deserialize(inputStream: CountingInputStream): Property.PropertyKey {

        val propertyType = propertyTypeDeserializer.deserialize(inputStream)

        val assetNameIndex = byteArrayOf(*inputStream.readNBytes(3), 0).toLittleEndianUInt()
        val assetName = deserializationContext.getAssetName(assetNameIndex)

        return Property.PropertyKey(
            propertyType = propertyType,
            name = assetName
        )
    }
}
