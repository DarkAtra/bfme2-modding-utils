package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.ConversionException
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import de.darkatra.bfme2.toLittleEndianUInt
import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.Deserializer
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DeserializationContextResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import org.apache.commons.io.input.CountingInputStream
import java.nio.charset.StandardCharsets

data class Property(
    val key: PropertyKey,
    val value: Any
) {

    data class PropertyKey(
        val propertyType: PropertyType,
        val name: String
    ) {

        enum class PropertyType {
            BOOLEAN,
            INTEGER,
            FLOAT,
            ASCII_STRING,
            UNICODE_STRING,

            // seems to be the same as ASCII_STRING
            UNKNOWN;

            companion object {

                fun ofByte(byte: Byte): PropertyType {
                    return when (byte) {
                        0.toByte() -> BOOLEAN
                        1.toByte() -> INTEGER
                        2.toByte() -> FLOAT
                        3.toByte() -> ASCII_STRING
                        4.toByte() -> UNICODE_STRING
                        5.toByte() -> UNKNOWN
                        else -> throw ConversionException("Unknown PropertyType for byte '$byte'.")
                    }
                }
            }
        }

    }

    internal class PropertyDeserializer(
        @Resolve(DeserializationContextResolver::class)
        private val context: DeserializationContext
    ) : Deserializer<Property> {

        override fun deserialize(inputStream: CountingInputStream): Property {

            val propertyType = PropertyKey.PropertyType.ofByte(inputStream.readByte())

            val assetNameIndex = byteArrayOf(*inputStream.readNBytes(3), 0).toLittleEndianUInt()
            val assetName = context.getAssetName(assetNameIndex)

            val propertyKey = PropertyKey(
                propertyType = propertyType,
                name = assetName
            )

            val value = when (propertyKey.propertyType) {
                PropertyKey.PropertyType.BOOLEAN -> inputStream.readBoolean()
                PropertyKey.PropertyType.INTEGER -> inputStream.readUInt()
                PropertyKey.PropertyType.FLOAT -> inputStream.readFloat()
                PropertyKey.PropertyType.ASCII_STRING -> inputStream.readUShortPrefixedString()
                PropertyKey.PropertyType.UNICODE_STRING -> inputStream.readUShortPrefixedString(StandardCharsets.UTF_16LE)
                PropertyKey.PropertyType.UNKNOWN -> inputStream.readUShortPrefixedString()
            }

            return Property(
                key = propertyKey,
                value = value
            )
        }
    }
}
