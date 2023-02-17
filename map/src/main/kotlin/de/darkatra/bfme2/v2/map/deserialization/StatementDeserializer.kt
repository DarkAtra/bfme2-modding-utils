package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.v2.map.Action
import de.darkatra.bfme2.v2.map.ActionFalse
import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.Condition
import de.darkatra.bfme2.v2.map.OrCondition
import de.darkatra.bfme2.v2.map.Statement
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.full.findAnnotation

internal class StatementDeserializer(
    deserializerFactory: DeserializerFactory,
    annotationProcessingContext: AnnotationProcessingContext,
    private val deserializationContext: DeserializationContext,
    private val postProcessor: PostProcessor<Statement>
) : Deserializer<Statement> {

    private val currentElementName = annotationProcessingContext.getCurrentElement().getName()

    private val deserializers = mapOf(
        Action::class.findAnnotation<Asset>()!!.name to deserializerFactory.getDeserializer(Action::class),
        ActionFalse::class.findAnnotation<Asset>()!!.name to deserializerFactory.getDeserializer(ActionFalse::class),
        Condition::class.findAnnotation<Asset>()!!.name to deserializerFactory.getDeserializer(Condition::class),
        OrCondition::class.findAnnotation<Asset>()!!.name to deserializerFactory.getDeserializer(OrCondition::class)
    )

    override fun deserialize(inputStream: CountingInputStream): Statement {
        val assetName = deserializationContext.peek().assetName
        val deserializer = deserializers[assetName] ?: failWithException(assetName)
        return deserializer.deserialize(inputStream).also {
            postProcessor.postProcess(it, deserializationContext)
        }
    }

    private fun failWithException(assetName: String): Nothing {
        throw InvalidDataException("Unexpected assetName '$assetName' reading $currentElementName. Expected one of: ${deserializers.keys}")
    }
}
