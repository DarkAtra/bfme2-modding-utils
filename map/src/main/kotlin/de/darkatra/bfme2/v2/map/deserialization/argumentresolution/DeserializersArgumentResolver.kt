package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.Deserializer
import de.darkatra.bfme2.v2.map.deserialization.model.Class
import de.darkatra.bfme2.v2.map.deserialization.model.ConstructorParameter
import de.darkatra.bfme2.v2.map.deserialization.model.Generic
import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

internal class DeserializersArgumentResolver(
    private val context: DeserializationContext
) : ArgumentResolver<List<Deserializer<*>>> {

    private val typeArgumentResolver = TypeArgumentResolver()

    override fun resolve(currentElement: ProcessableElement): List<Deserializer<*>> {

        return when (currentElement) {
            is Generic -> getDeserializersForType(currentElement)
            is ConstructorParameter -> getDeserializersForType(currentElement)
            is Class -> getDeserializersForConstructorParameters(currentElement)
            else -> error("${DeserializersArgumentResolver::class.simpleName} only supports resolution on Generics, ConstructorParameters and Classes")
        }
    }

    private fun getDeserializersForType(currentElement: ProcessableElement): List<Deserializer<*>> {

        // if the current type has no further generics, resolve constructor parameters
        val type = currentElement.getType()
        if (type.arguments.isEmpty()) {
            val targetClass = typeArgumentResolver.resolve(currentElement)
            return getDeserializersForConstructorParameters(Class(targetClass))
        }

        // otherwise continue the generic chain
        return type.arguments.mapIndexed { genericIndex, typeArgument ->

            val genericType = typeArgument.type
                ?: error("Could not resolve generic at index $genericIndex of '${currentElement.getName()}'.")

            val nextElement = context.beginProcessing(Generic(currentElement, genericIndex, genericType))
            context.deserializerFactory.getDeserializer(nextElement).also {
                context.endProcessingCurrentElement()
            }
        }
    }

    private fun getDeserializersForConstructorParameters(clazz: Class): List<Deserializer<*>> {

        val primaryConstructor = clazz.clazz.primaryConstructor
            ?: error("${clazz.getName()} is required to have a primary constructor.")

        val parameters = primaryConstructor.valueParameters
        return parameters.map { parameter ->
            context.setCurrentParameter(parameter)
            val nextElement = context.beginProcessing(ConstructorParameter(parameter))
            context.deserializerFactory.getDeserializer(nextElement).also {
                context.endProcessingCurrentElement()
            }
        }
    }
}
