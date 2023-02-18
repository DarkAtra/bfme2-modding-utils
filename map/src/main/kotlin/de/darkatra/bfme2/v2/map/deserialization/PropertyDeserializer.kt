package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import de.darkatra.bfme2.v2.map.Property
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import java.nio.charset.StandardCharsets

internal class PropertyDeserializer(
    deserializerFactory: DeserializerFactory,
    private val deserializationContext: DeserializationContext,
    private val postProcessor: PostProcessor<Property>
) : Deserializer<Property> {

    private val propertyKeyDeserializer = PropertyKeyDeserializer(deserializerFactory, deserializationContext)

    override fun deserialize(inputStream: CountingInputStream): Property {

        val propertyKey = propertyKeyDeserializer.deserialize(inputStream)

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
        ).also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }
}
