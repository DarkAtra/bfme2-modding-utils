package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionHolder
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import java.io.OutputStream
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KVisibility
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

    private val primaryConstructor = classOfT.primaryConstructor
        ?: error("${classOfT.simpleName} is required to have a primary constructor.")

    private val parameters = primaryConstructor.valueParameters
    private val assetAnnotation = classOfT.findAnnotation<AssetAnnotation>()

    private val currentElementName = annotationProcessingContext.getCurrentElement().getName()

    override fun collectDataSections(data: T): DataSection {
        return DataSectionHolder(
            containingData = parameters.mapIndexed { index, parameter ->

                val fieldForParameter = classOfT.members
                    .filterIsInstance<KProperty<*>>()
                    .first { field -> field.name == parameter.name }

                if (fieldForParameter.getter.visibility == KVisibility.PUBLIC || fieldForParameter.getter.visibility == KVisibility.INTERNAL) {
                    @Suppress("UNCHECKED_CAST")
                    val serde = serdes[index] as Serde<Any>
                    val fieldData = fieldForParameter.getter.call(data)!!
                    serde.collectDataSections(fieldData)
                } else {
                    throw IllegalStateException("Could not collect data for parameter '${parameter.name}' because it's getter is not public or internal.")
                }
            },
            assetName = assetAnnotation?.name,
            assetVersion = assetAnnotation?.version
        )
    }

    override fun serialize(outputStream: OutputStream, data: T) {
        TODO("Not yet implemented")
    }

    override fun deserialize(inputStream: CountingInputStream): T {

        if (assetAnnotation != null) {
            val currentAsset = serializationContext.peek()
            if (assetAnnotation.name != currentAsset.assetName) {
                throw InvalidDataException("Unexpected assetName '${currentAsset.assetName}' reading $currentElementName. Expected: '${assetAnnotation.name}'")
            }
            if (assetAnnotation.version != currentAsset.assetVersion) {
                throw InvalidDataException("Unexpected assetVersion '${currentAsset.assetVersion}' for assetName '${currentAsset.assetName}' reading $currentElementName. Expected: '${assetAnnotation.version}'")
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
