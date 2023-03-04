package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.property.Property
import de.darkatra.bfme2.map.property.PropertyType
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
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
        ).also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }
}
