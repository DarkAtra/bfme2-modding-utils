package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.map.property.Property
import de.darkatra.bfme2.map.property.PropertyType
import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionHolder
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.map.serialization.postprocessing.NoopPostProcessor
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.NoopPreProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import de.darkatra.bfme2.writeBoolean
import de.darkatra.bfme2.writeFloat
import de.darkatra.bfme2.writeUInt
import de.darkatra.bfme2.writeUShortPrefixedString
import java.io.OutputStream
import java.nio.charset.StandardCharsets

internal class PropertySerde(
    serdeFactory: SerdeFactory,
    private val serializationContext: SerializationContext,
    private val preProcessor: PreProcessor<Property>,
    private val postProcessor: PostProcessor<Property>
) : Serde<Property> {

    private val propertyKeySerde = PropertyKeySerde(serdeFactory, serializationContext, NoopPreProcessor(), NoopPostProcessor())

    override fun collectDataSections(data: Property): DataSection {
        return DataSectionHolder(
            containingData = listOf(
                propertyKeySerde.collectDataSections(data.key),
                when (data.key.propertyType) {
                    PropertyType.BOOLEAN -> DataSectionLeaf.BOOLEAN
                    PropertyType.INTEGER -> DataSectionLeaf.INT
                    PropertyType.FLOAT -> DataSectionLeaf.FLOAT
                    PropertyType.ASCII_STRING -> DataSectionLeaf(2L + (data.value as String).length)
                    PropertyType.UNICODE_STRING -> DataSectionLeaf(2L + ((data.value as String).length * 2))
                    PropertyType.UNKNOWN -> DataSectionLeaf(2L + (data.value as String).length)
                }
            )
        )
    }

    override fun serialize(outputStream: OutputStream, data: Property) {

        preProcessor.preProcess(data, serializationContext).let { property ->

            propertyKeySerde.serialize(outputStream, property.key)

            when (property.key.propertyType) {
                PropertyType.BOOLEAN -> outputStream.writeBoolean(property.value as Boolean)
                PropertyType.INTEGER -> outputStream.writeUInt(property.value as UInt)
                PropertyType.FLOAT -> outputStream.writeFloat(property.value as Float)
                PropertyType.ASCII_STRING -> outputStream.writeUShortPrefixedString(property.value as String)
                PropertyType.UNICODE_STRING -> outputStream.writeUShortPrefixedString(property.value as String, StandardCharsets.UTF_16LE)
                PropertyType.UNKNOWN -> outputStream.writeUShortPrefixedString(property.value as String)
            }
        }
    }

    override fun deserialize(inputStream: CountingInputStream): Property {

        val propertyKey = propertyKeySerde.deserialize(inputStream)

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
            postProcessor.postProcess(it, serializationContext)
        }
    }
}
