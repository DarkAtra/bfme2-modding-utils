package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.serialization.model.DataSectionHolder
import de.darkatra.bfme2.map.toKClass
import java.io.OutputStream
import kotlin.reflect.KProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

internal class MapFileSerde(
    private val serializationContext: SerializationContext,
    private val serdes: List<Serde<*>>
) : Serde<MapFile> {

    private val primaryConstructor = MapFile::class.primaryConstructor
        ?: error("${MapFile::class.simpleName} is required to have a primary constructor.")

    private val parameters = primaryConstructor.valueParameters
    private val parameterToField = parameters.associateWith { parameter ->
        val fieldForParameter = MapFile::class.members
            .filterIsInstance<KProperty<*>>()
            .find { field -> field.name == parameter.name }!!
        if (fieldForParameter.getter.visibility != KVisibility.PUBLIC && fieldForParameter.getter.visibility != KVisibility.INTERNAL) {
            throw IllegalStateException("Field for parameter '${parameter.name}' is not public or internal.")
        }
        fieldForParameter
    }

    override fun calculateDataSection(data: MapFile): DataSectionHolder {

        return DataSectionHolder(
            containingData = parameterToField.entries.mapIndexed { index, (p, fieldForParameter) ->
                @Suppress("UNCHECKED_CAST")
                val serde = serdes[index] as Serde<Any>
                val fieldData = fieldForParameter.getter.call(data)!!
                serde.calculateDataSection(fieldData)
            },
            assetName = "MapFile"
        )
    }

    @OptIn(ExperimentalTime::class)
    override fun serialize(outputStream: OutputStream, data: MapFile) {

        // TODO: check if we need to preserve the order in which we write the data
        parameterToField.entries.forEachIndexed { index, (parameter, fieldForParameter) ->
            @Suppress("UNCHECKED_CAST")
            val serde = serdes[index] as Serde<Any>
            val fieldData = fieldForParameter.getter.call(data)!!

            measureTime {
                MapFileWriter.writeAsset(outputStream, serializationContext, fieldData)
                serde.serialize(outputStream, fieldData)
            }.also { elapsedTime ->
                if (serializationContext.debugMode) {
                    println("Deserialization of '${parameter.name}' took $elapsedTime.")
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    override fun deserialize(inputStream: CountingInputStream): MapFile {

        val assetNameToParameterIndexList = parameters.mapIndexed { parameterIndex, parameter ->
            val assetAnnotation = parameter.type.toKClass().findAnnotation<Asset>()
                ?: error("All properties in the primary constructor of '${MapFile::class.simpleName}' must be annotated with '${Asset::class.simpleName}'.")

            Pair(assetAnnotation.name, parameterIndex)
        }

        val values = arrayOfNulls<Any>(parameters.size)
        MapFileReader.readAssets(inputStream, serializationContext) { assetName ->
            val parameterIndex = assetNameToParameterIndexList
                .find { it.first == assetName }
                ?.second

            if (parameterIndex != null) {
                measureTime {
                    values[parameterIndex] = serdes[parameterIndex].deserialize(inputStream)!!
                }.also { elapsedTime ->
                    if (serializationContext.debugMode) {
                        println("Deserialization of '$assetName' took $elapsedTime.")
                    }
                }
            } else {
                error("Asset with name '$assetName' is not implemented yet.")
            }
        }

        return primaryConstructor.call(*values)
    }
}
