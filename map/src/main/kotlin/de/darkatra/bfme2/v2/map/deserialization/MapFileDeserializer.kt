package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.MapFile
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.TypeArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

internal class MapFileDeserializer(
    private val deserializationContext: DeserializationContext,
    private val deserializers: List<Deserializer<*>>,
    private val postProcessor: PostProcessor<MapFile>
) : Deserializer<MapFile> {

    private val classOfT: KClass<MapFile> = MapFile::class

    @OptIn(ExperimentalTime::class)
    override fun deserialize(inputStream: CountingInputStream): MapFile {

        val primaryConstructor = classOfT.primaryConstructor
            ?: error("${classOfT.simpleName} is required to have a primary constructor.")

        val parameters = primaryConstructor.valueParameters

        val assetNameToParameterIndexList = parameters.mapIndexed { parameterIndex, parameter ->
            val assetAnnotation = getClassOfParameter(parameter).findAnnotation<Asset>()
                ?: error("All properties in the primary constructor of '${classOfT::class.simpleName}' must be annotated with '${Asset::class.simpleName}'.")

            Pair(assetAnnotation.name, parameterIndex)
        }

        val values = arrayOfNulls<Any>(parameters.size)
        MapFileReader.readAssets(inputStream, deserializationContext) { assetName ->
            val parameterIndex = assetNameToParameterIndexList
                .find { it.first == assetName }
                ?.second

            if (parameterIndex != null) {
                measureTime {
                    values[parameterIndex] = deserializers[parameterIndex].deserialize(inputStream)!!
                }.also { elapsedTime ->
                    if (deserializationContext.debugMode) {
                        println("Deserialization of '$assetName' took $elapsedTime.")
                    }
                }
            } else {
                if (!deserializationContext.debugMode) {
                    error("Asset with name '$assetName' is not implemented yet.")
                } else {
                    inputStream.readAllBytes()
                }
            }
        }

        return primaryConstructor.call(*values).also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }

    private fun getClassOfParameter(parameter: KParameter): KClass<*> {
        val classOfT = parameter.type.classifier
        if (classOfT !is KClass<*>) {
            error("${TypeArgumentResolver::class.simpleName} only supports resolving types for classes.")
        }
        return classOfT
    }
}
