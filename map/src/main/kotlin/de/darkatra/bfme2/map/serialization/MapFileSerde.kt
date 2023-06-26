package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
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
    private val serdes: List<Serde<*>>,
    private val postProcessor: PostProcessor<MapFile>
) : Serde<MapFile> {

    private val primaryConstructor = MapFile::class.primaryConstructor
        ?: error("${MapFile::class.simpleName} is required to have a primary constructor.")

    private val parameters = primaryConstructor.valueParameters

    override fun calculateByteCount(data: MapFile): Long {

        return parameters.mapIndexed { index, parameter ->

            val fieldForParameter = MapFile::class.members
                .filterIsInstance<KProperty<*>>()
                .first { field -> field.name == parameter.name }

            if (fieldForParameter.getter.visibility == KVisibility.PUBLIC || fieldForParameter.getter.visibility == KVisibility.INTERNAL) {
                @Suppress("UNCHECKED_CAST")
                val serde = serdes[index] as Serde<Any>
                val fieldData = fieldForParameter.getter.call(data)!!
                4 + 2 + 4 + serde.calculateByteCount(fieldData)
            } else {
                throw IllegalStateException("Could not calculate byte count for parameter '${parameter.name}' because it's getter is not public or internal.")
            }
        }.sum()
    }

    override fun serialize(outputStream: OutputStream, data: MapFile) {
        TODO("Not yet implemented")
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

        return primaryConstructor.call(*values).also {
            postProcessor.postProcess(it, serializationContext)
        }
    }
}
