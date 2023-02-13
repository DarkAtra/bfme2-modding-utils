package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import de.darkatra.bfme2.toLittleEndianUInt
import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.Deserializer
import org.apache.commons.io.input.CountingInputStream
import java.nio.charset.StandardCharsets

data class Property(
    val key: PropertyKey,
    val value: Any
) {

    class PropertyDeserializer : Deserializer<Property> {

        override fun deserialize(inputStream: CountingInputStream, deserializationContext: DeserializationContext): Property {

            val propertyType = PropertyType.ofByte(inputStream.readByte())

            val assetNameIndex = byteArrayOf(*inputStream.readNBytes(3), 0).toLittleEndianUInt()
            val assetName = deserializationContext.getAssetName(assetNameIndex)

            val propertyKey = PropertyKey(
                propertyType = propertyType,
                name = assetName
            )

            val value = when (propertyKey.propertyType) {
                PropertyType.BOOLEAN -> inputStream.readBoolean()
                PropertyType.INTEGER -> inputStream.readUInt()
                PropertyType.FLOAT -> inputStream.readFloat()
                PropertyType.ASCII_STRING -> inputStream.readUShortPrefixedString()
                PropertyType.UNICODE_STRING -> inputStream.readUShortPrefixedString(StandardCharsets.UTF_16LE)
                PropertyType.UNKNOWN -> inputStream.readUShortPrefixedString()
            }

            return Property(
                key = propertyKey,
                value = value
            )
        }
    }
}
