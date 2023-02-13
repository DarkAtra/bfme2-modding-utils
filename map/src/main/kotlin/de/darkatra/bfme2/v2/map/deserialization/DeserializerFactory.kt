package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.typeOf

internal class DeserializerFactory {

    private val defaultDeserializers: Map<KType, KClass<out Deserializer<*>>> = mapOf(
        typeOf<Boolean>() to BooleanDeserializer::class,
        typeOf<UShort>() to UShortDeserializer::class,
        typeOf<Short>() to ShortDeserializer::class,
        typeOf<Int>() to IntDeserializer::class,
        typeOf<UInt>() to UIntDeserializer::class,
        typeOf<Float>() to FloatDeserializer::class,
        typeOf<String>() to StringDeserializer::class,
        typeOf<List<*>>() to ListDeserializer::class,
        typeOf<Map<*, *>>() to MapDeserializer::class,
        typeOf<Any>() to ObjectDeserializer::class
    )

    internal lateinit var context: DeserializationContext

    internal fun getDeserializer(currentElement: ProcessableElement): Deserializer<*> {

        val type = currentElement.getType()

        val deserializerClass = type.findAnnotation<Deserialize>()?.using
            ?: getDeserializerByType(type)
            ?: error("No suitable deserializer found for '${currentElement.getName()}'.")

        val deserializerConstructor = deserializerClass.primaryConstructor
            ?: error("${deserializerClass.simpleName} is required to have a primary constructor.")

        val deserializerArguments = mutableListOf<Any?>()
        deserializerConstructor.valueParameters.forEach { deserializerParameter ->

            val resolveAnnotation = deserializerParameter.findAnnotation<Resolve>()
                ?: error("Required annotation '${Resolve::class.simpleName}' is missing on parameter '${deserializerParameter}' in deserializer '${deserializerClass.simpleName}'.")

            val argumentResolverClass = resolveAnnotation.using
            val values = argumentResolverClass.primaryConstructor!!.parameters.mapIndexed { parameterIndex, parameter ->
                when (parameter.type) {
                    typeOf<DeserializationContext>() -> context
                    typeOf<KClass<Deserializer<*>>>() -> deserializerClass
                    typeOf<KParameter>() -> deserializerParameter
                    else -> error("Unable to resolve argument of type '${parameter.type}' (pos: $parameterIndex) of class ${argumentResolverClass.simpleName}.")
                }
            }

            val argumentResolverInstance = argumentResolverClass.primaryConstructor!!.call(*values.toTypedArray())
            deserializerArguments.add(argumentResolverInstance.resolve(currentElement))
        }

        return deserializerConstructor.call(*deserializerArguments.toTypedArray())
    }

    private fun getDeserializerByType(type: KType): KClass<out Deserializer<*>>? {
        return defaultDeserializers.entries
            .find { (deserializerType, _) -> deserializerType.isSupertypeOf(type) }
            ?.value
    }
}
