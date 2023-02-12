package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.typeOf

class DeserializerFactory {

    private val defaultDeserializers: Map<KType, KClass<out Deserializer<*>>> = mapOf(
        typeOf<UShort>() to UShortDeserializer::class,
        typeOf<UInt>() to UIntDeserializer::class,
        typeOf<List<*>>() to ListDeserializer::class,
        typeOf<Map<*, *>>() to MapDeserializer::class,
        typeOf<Any>() to ObjectDeserializer::class
    )

    fun getDeserializer(deserializationContext: DeserializationContext): Deserializer<*> {

        val type = deserializationContext.getCurrentElement().getType()

        val deserializerClass = type.findAnnotation<Deserialize>()?.using
            ?: getDeserializerByType(type)
            ?: error("No suitable deserializer found for '${deserializationContext.getCurrentParameter().name}' in primary constructor of class '${deserializationContext.getCurrentType().simpleName}'.")

        val deserializerConstructor = deserializerClass.primaryConstructor
            ?: error("${deserializerClass.simpleName} is required to have a primary constructor.")

        val deserializerArguments = mutableListOf<Any?>()
        deserializerConstructor.valueParameters.forEach { deserializerParameter ->
            val resolveAnnotation = deserializerParameter.findAnnotation<Resolve>()
                ?: error("Required annotation '${Resolve::class.simpleName}' is missing on parameter '${deserializerParameter}' in deserializer '${deserializerClass.simpleName}'.")

            val argumentResolverInstance = resolveAnnotation.using.createInstance()
            deserializerArguments.add(argumentResolverInstance.resolve(deserializationContext))
        }

        return deserializerConstructor.call(*deserializerArguments.toTypedArray())
    }

    private fun getDeserializerByType(type: KType): KClass<out Deserializer<*>>? {
        return defaultDeserializers.entries
            .find { (deserializerType, _) -> deserializerType.isSupertypeOf(type) }
            ?.value
    }
}
