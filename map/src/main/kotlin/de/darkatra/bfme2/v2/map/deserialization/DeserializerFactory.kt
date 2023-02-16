package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.v2.map.Property
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.ArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.DefaultArgumentResolver
import de.darkatra.bfme2.v2.map.deserialization.argumentresolution.Resolve
import de.darkatra.bfme2.v2.map.deserialization.model.Class
import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.typeOf

internal class DeserializerFactory(
    private val annotationProcessingContext: AnnotationProcessingContext,
    private val deserializationContext: DeserializationContext
) {

    private val defaultDeserializers: Map<KType, KClass<out Deserializer<*>>> = mapOf(
        typeOf<Byte>() to ByteDeserializer::class,
        typeOf<Boolean>() to BooleanDeserializer::class,
        typeOf<UShort>() to UShortDeserializer::class,
        typeOf<Short>() to ShortDeserializer::class,
        typeOf<Int>() to IntDeserializer::class,
        typeOf<UInt>() to UIntDeserializer::class,
        typeOf<Float>() to FloatDeserializer::class,
        typeOf<String>() to StringDeserializer::class,
        typeOf<List<*>>() to ListDeserializer::class,
        typeOf<Map<*, *>>() to MapDeserializer::class,
        typeOf<Enum<*>>() to EnumDeserializer::class,
        typeOf<Property>() to PropertyDeserializer::class,
        typeOf<Any>() to ObjectDeserializer::class
    )

    internal fun <T : Any> getDeserializer(clazz: KClass<T>): Deserializer<T> {
        @Suppress("UNCHECKED_CAST")
        return getDeserializer(Class(clazz)) as Deserializer<T>
    }

    internal fun getDeserializer(currentElement: ProcessableElement): Deserializer<*> {

        val type = currentElement.getType()

        val deserializerClass = type.findAnnotation<Deserialize>()?.using
            ?: getDeserializerByClass(currentElement)
            ?: getDeserializerByType(type)
            ?: error("No suitable deserializer found for '${currentElement.getName()}'.")

        val deserializerConstructor = deserializerClass.primaryConstructor
            ?: error("${deserializerClass.simpleName} is required to have a primary constructor.")

        if (annotationProcessingContext.debugMode) {
            println("Using '${deserializerClass.simpleName}' for '${currentElement.getName()}' (${currentElement.getType()}).")
        }

        val deserializerArguments = mutableListOf<Any?>()
        deserializerConstructor.valueParameters.forEach { deserializerParameter ->

            val resolveAnnotation = deserializerParameter.findAnnotation<Resolve>()

            val argumentResolverClass = resolveAnnotation?.using ?: DefaultArgumentResolver::class
            val argumentResolverInstance = getArgumentResolver(argumentResolverClass, deserializerClass, deserializerParameter)

            if (annotationProcessingContext.debugMode) {
                println("* Resolving parameter '${deserializerParameter.name}' using '${argumentResolverInstance::class.simpleName}'.")
            }

            deserializerArguments.add(argumentResolverInstance.resolve(currentElement))
        }

        return deserializerConstructor.call(*deserializerArguments.toTypedArray())
    }

    internal fun getArgumentResolver(
        argumentResolverClass: KClass<out ArgumentResolver<*>>,
        deserializerClass: KClass<out Deserializer<*>>,
        deserializerParameter: KParameter
    ): ArgumentResolver<*> {

        val values = argumentResolverClass.primaryConstructor!!.parameters.mapIndexed { parameterIndex, parameter ->
            when (parameter.type) {
                typeOf<AnnotationProcessingContext>() -> annotationProcessingContext
                typeOf<DeserializationContext>() -> deserializationContext
                typeOf<DeserializerFactory>() -> this
                typeOf<KClass<Deserializer<*>>>() -> deserializerClass
                typeOf<KParameter>() -> deserializerParameter
                else -> error("Unable to resolve argument of type '${parameter.type}' (pos: $parameterIndex) of class ${argumentResolverClass.simpleName}.")
            }
        }

        return argumentResolverClass.primaryConstructor!!.call(*values.toTypedArray())
    }

    private fun getDeserializerByClass(currentElement: ProcessableElement): KClass<out Deserializer<*>>? {
        return when (currentElement) {
            is Class -> currentElement.clazz.findAnnotation<Deserialize>()?.using
            else -> null
        }
    }

    private fun getDeserializerByType(type: KType): KClass<out Deserializer<*>>? {
        return defaultDeserializers.entries
            .find { (deserializerType, _) -> deserializerType.isSupertypeOf(type) }
            ?.value
    }
}
