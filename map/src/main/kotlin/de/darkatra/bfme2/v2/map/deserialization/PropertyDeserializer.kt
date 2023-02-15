package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import de.darkatra.bfme2.toLittleEndianUInt
import de.darkatra.bfme2.v2.map.Property
import org.apache.commons.io.input.CountingInputStream
import java.nio.charset.StandardCharsets

internal class PropertyDeserializer(
    private val context: DeserializationContext
) : Deserializer<Property> {

    override fun deserialize(inputStream: CountingInputStream): Property {

        val propertyType = Property.PropertyKey.PropertyType.ofByte(inputStream.readByte())

        val assetNameIndex = byteArrayOf(*inputStream.readNBytes(3), 0).toLittleEndianUInt()
        val assetName = context.getAssetName(assetNameIndex)

        val propertyKey = Property.PropertyKey(
            propertyType = propertyType,
            name = assetName
        )

        val value = when (propertyKey.propertyType) {
            Property.PropertyKey.PropertyType.BOOLEAN -> inputStream.readBoolean()
            Property.PropertyKey.PropertyType.INTEGER -> inputStream.readUInt()
            Property.PropertyKey.PropertyType.FLOAT -> inputStream.readFloat()
            Property.PropertyKey.PropertyType.ASCII_STRING -> inputStream.readUShortPrefixedString()
            Property.PropertyKey.PropertyType.UNICODE_STRING -> inputStream.readUShortPrefixedString(StandardCharsets.UTF_16LE)
            Property.PropertyKey.PropertyType.UNKNOWN -> inputStream.readUShortPrefixedString()
        }

        return Property(
            key = propertyKey,
            value = value
        )
    }
}
