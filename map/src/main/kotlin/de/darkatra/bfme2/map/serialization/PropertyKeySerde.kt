package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.map.property.PropertyKey
import de.darkatra.bfme2.map.property.PropertyType
import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionHolder
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.toLittleEndianUInt
import java.io.OutputStream

internal class PropertyKeySerde(
    serdeFactory: SerdeFactory,
    private val serializationContext: SerializationContext,
    private val preProcessor: PreProcessor<PropertyKey>,
    private val postProcessor: PostProcessor<PropertyKey>
) : Serde<PropertyKey> {

    private val propertyTypeSerde: Serde<PropertyType> = serdeFactory.getSerde(PropertyType::class)

    override fun collectDataSections(data: PropertyKey): DataSection {
        return DataSectionHolder(
            containingData = listOf(
                DataSectionLeaf(3),
                propertyTypeSerde.collectDataSections(data.propertyType)
            )
        )
    }

    override fun serialize(outputStream: OutputStream, data: PropertyKey) {

        preProcessor.preProcess(data, serializationContext).let { propertyKey ->

            propertyTypeSerde.serialize(outputStream, propertyKey.propertyType)

            // TODO: serialize assetNameIndex
        }
    }

    override fun deserialize(inputStream: CountingInputStream): PropertyKey {

        val propertyType = propertyTypeSerde.deserialize(inputStream)

        val assetNameIndex = byteArrayOf(*inputStream.readNBytes(3), 0).toLittleEndianUInt()
        val assetName = serializationContext.getAssetName(assetNameIndex)

        return PropertyKey(
            propertyType = propertyType,
            name = assetName
        ).also {
            postProcessor.postProcess(it, serializationContext)
        }
    }
}
