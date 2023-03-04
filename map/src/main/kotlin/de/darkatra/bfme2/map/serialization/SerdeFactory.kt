package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.map.property.Property
import de.darkatra.bfme2.map.property.PropertyKey
import de.darkatra.bfme2.map.serialization.argumentresolution.ArgumentResolver
import de.darkatra.bfme2.map.serialization.argumentresolution.DefaultArgumentResolver
import de.darkatra.bfme2.map.serialization.argumentresolution.Resolve
import de.darkatra.bfme2.map.serialization.model.Class
import de.darkatra.bfme2.map.serialization.model.Generic
import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import de.darkatra.bfme2.map.toKClass
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.typeOf

internal class SerdeFactory(
    private val annotationProcessingContext: AnnotationProcessingContext,
    private val serializationContext: SerializationContext
) {

    private val defaultSerdes: Map<KType, KClass<out Serde<*>>> = mapOf(
        typeOf<Byte>() to ByteSerde::class,
        typeOf<Boolean>() to BooleanSerde::class,
        typeOf<UShort>() to UShortSerde::class,
        typeOf<Short>() to ShortSerde::class,
        typeOf<Int>() to IntSerde::class,
        typeOf<UInt>() to UIntSerde::class,
        typeOf<Float>() to FloatSerde::class,
        typeOf<String>() to UShortPrefixedStringSerde::class,
        typeOf<List<*>>() to ListSerde::class,
        typeOf<Map<*, *>>() to MapSerde::class,
        typeOf<Enum<*>>() to EnumSerde::class,
        typeOf<Color>() to ColorSerde::class,
        typeOf<Property>() to PropertySerde::class,
        typeOf<PropertyKey>() to PropertyKeySerde::class,
        typeOf<Any>() to ObjectSerde::class
    )

    internal fun <T : Any> getSerde(clazz: KClass<T>): Serde<T> {
        @Suppress("UNCHECKED_CAST")
        return getSerde(Class(clazz)) as Serde<T>
    }

    internal fun getSerde(currentElement: ProcessableElement): Serde<*> {

        val type = currentElement.getType()

        val serdeClass = type.findAnnotation<Serialize>()?.using
            ?: getSerdeByClass(currentElement)
            ?: getSerdeByType(type)
            ?: error("No suitable serde found for '${currentElement.getName()}'.")

        val serdeConstructor = serdeClass.primaryConstructor
            ?: error("${serdeClass.simpleName} is required to have a primary constructor.")

        if (annotationProcessingContext.debugMode) {
            println("Using '${serdeClass.simpleName}' for '${currentElement.getName()}' (${currentElement.getType()}).")
        }

        val serdeArguments = mutableListOf<Any?>()
        serdeConstructor.valueParameters.forEach { serdeParameter ->

            val resolveAnnotation = serdeParameter.findAnnotation<Resolve>()

            val argumentResolverClass = resolveAnnotation?.using ?: DefaultArgumentResolver::class
            val argumentResolverInstance = getArgumentResolver(argumentResolverClass, serdeClass, serdeParameter)

            if (annotationProcessingContext.debugMode) {
                println("* Resolving parameter '${serdeParameter.name}' using '${argumentResolverInstance::class.simpleName}'.")
            }

            serdeArguments.add(argumentResolverInstance.resolve(currentElement))
        }

        return serdeConstructor.call(*serdeArguments.toTypedArray())
    }

    internal fun getArgumentResolver(
        argumentResolverClass: KClass<out ArgumentResolver<*>>,
        serdeClass: KClass<out Serde<*>>,
        serdeParameter: KParameter
    ): ArgumentResolver<*> {

        val values = argumentResolverClass.primaryConstructor!!.parameters.mapIndexed { parameterIndex, parameter ->
            when (parameter.type) {
                typeOf<AnnotationProcessingContext>() -> annotationProcessingContext
                typeOf<SerializationContext>() -> serializationContext
                typeOf<SerdeFactory>() -> this
                typeOf<KClass<Serde<*>>>() -> serdeClass
                typeOf<KParameter>() -> serdeParameter
                else -> error("Unable to resolve argument of type '${parameter.type}' (pos: $parameterIndex) of class ${argumentResolverClass.simpleName}.")
            }
        }

        return argumentResolverClass.primaryConstructor!!.call(*values.toTypedArray())
    }

    private fun getSerdeByClass(currentElement: ProcessableElement): KClass<out Serde<*>>? {
        return when (currentElement) {
            is Class -> currentElement.clazz.findAnnotation<Serialize>()?.using
            is Generic -> when (currentElement.getType().arguments.size) {
                0 -> currentElement.getType().toKClass().findAnnotation<Serialize>()?.using
                else -> null
            }

            else -> null
        }
    }

    private fun getSerdeByType(type: KType): KClass<out Serde<*>>? {
        return defaultSerdes.entries
            .find { (serdeType, _) -> serdeType.isSupertypeOf(type) }
            ?.value
    }
}
