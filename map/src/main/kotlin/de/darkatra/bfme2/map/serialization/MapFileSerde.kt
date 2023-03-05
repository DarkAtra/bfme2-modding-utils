package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.toKClass
import org.apache.commons.io.input.CountingInputStream
import java.io.OutputStream
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

    override fun calculateByteCount(data: MapFile): Long {
        TODO("Not yet implemented")
    }

    override fun serialize(outputStream: OutputStream, data: MapFile) {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalTime::class)
    override fun deserialize(inputStream: CountingInputStream): MapFile {

        val primaryConstructor = MapFile::class.primaryConstructor
            ?: error("${MapFile::class.simpleName} is required to have a primary constructor.")

        val parameters = primaryConstructor.valueParameters

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
