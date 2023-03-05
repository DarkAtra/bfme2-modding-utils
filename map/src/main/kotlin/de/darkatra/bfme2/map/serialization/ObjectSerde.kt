package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import java.io.OutputStream
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import de.darkatra.bfme2.map.Asset as AssetAnnotation

internal class ObjectSerde<T : Any>(
    annotationProcessingContext: AnnotationProcessingContext,
    private val classOfT: KClass<T>,
    private val serializationContext: SerializationContext,
    private val serdes: List<Serde<*>>,
    private val postProcessor: PostProcessor<T>
) : Serde<T> {

    private val currentElementName = annotationProcessingContext.getCurrentElement().getName()

    override fun calculateByteCount(data: T): Long {
        TODO("Not yet implemented")
    }

    override fun serialize(outputStream: OutputStream, data: T) {
        TODO("Not yet implemented")
    }

    override fun deserialize(inputStream: CountingInputStream): T {

        val primaryConstructor = classOfT.primaryConstructor
            ?: error("${classOfT.simpleName} is required to have a primary constructor.")

        val expectedAsset = classOfT.findAnnotation<AssetAnnotation>()
        val parameters = primaryConstructor.valueParameters

        if (expectedAsset != null) {
            val currentAsset = serializationContext.peek()
            if (expectedAsset.name != currentAsset.assetName) {
                throw InvalidDataException("Unexpected assetName '${currentAsset.assetName}' reading $currentElementName. Expected: '${expectedAsset.name}'")
            }
            if (expectedAsset.version != currentAsset.assetVersion) {
                throw InvalidDataException("Unexpected assetVersion '${currentAsset.assetVersion}' for assetName '${currentAsset.assetName}' reading $currentElementName. Expected: '${expectedAsset.version}'")
            }
        }

        val values = parameters.mapIndexed { parameterIndex, parameter ->
            try {
                serdes[parameterIndex].deserialize(inputStream)
            } catch (e: Exception) {
                throw InvalidDataException(
                    "Error deserializing value for '${classOfT.simpleName}#${parameter.name}' using '${serdes[parameterIndex]::class.simpleName}'.",
                    e
                )
            }
        }

        return try {
            primaryConstructor.call(*values.toTypedArray()).also {
                postProcessor.postProcess(it, serializationContext)
            }
        } catch (e: Exception) {
            throw InvalidDataException(
                """Error deserializing value for '${classOfT.simpleName}' using '${ObjectSerde::class.simpleName}'.
                    |Expected: ${parameters.map { (it.type.classifier as KClass<*>).simpleName }}
                    |Found:    ${values.map { it?.let { it::class.simpleName } ?: Nothing::class.simpleName }}
                """.trimMargin(),
                e
            )
        }
    }
}
