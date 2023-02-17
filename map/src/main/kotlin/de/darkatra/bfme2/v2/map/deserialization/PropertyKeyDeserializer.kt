package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.toLittleEndianUInt
import de.darkatra.bfme2.v2.map.Property
import org.apache.commons.io.input.CountingInputStream

internal class PropertyKeyDeserializer(
    private val deserializationContext: DeserializationContext,
) : Deserializer<Property.PropertyKey> {

    override fun deserialize(inputStream: CountingInputStream): Property.PropertyKey {

        val propertyType = Property.PropertyKey.PropertyType.ofByte(inputStream.readByte())

        val assetNameIndex = byteArrayOf(*inputStream.readNBytes(3), 0).toLittleEndianUInt()
        val assetName = deserializationContext.getAssetName(assetNameIndex)

        return Property.PropertyKey(
            propertyType = propertyType,
            name = assetName
        )
    }
}
