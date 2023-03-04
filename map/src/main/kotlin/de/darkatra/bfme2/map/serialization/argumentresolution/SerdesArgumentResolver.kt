package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.AnnotationProcessingContext
import de.darkatra.bfme2.map.serialization.Serde
import de.darkatra.bfme2.map.serialization.SerdeFactory
import de.darkatra.bfme2.map.serialization.model.Class
import de.darkatra.bfme2.map.serialization.model.ConstructorParameter
import de.darkatra.bfme2.map.serialization.model.Generic
import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import de.darkatra.bfme2.map.toKClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

internal class SerdesArgumentResolver(
    private val annotationProcessingContext: AnnotationProcessingContext,
    private val serdeFactory: SerdeFactory
) : ArgumentResolver<List<Serde<*>>> {

    override fun resolve(currentElement: ProcessableElement): List<Serde<*>> {

        return when (currentElement) {
            is Generic -> getSerdesForType(currentElement)
            is ConstructorParameter -> getSerdesForType(currentElement)
            is Class -> getSerdesForInterfaceOrConstructorParameters(currentElement)
            else -> error("${SerdesArgumentResolver::class.simpleName} only supports resolution on Generics, ConstructorParameters and Classes")
        }
    }

    private fun getSerdesForType(currentElement: ProcessableElement): List<Serde<*>> {

        // if the current type has no further generics, resolve constructor parameters
        val type = currentElement.getType()
        if (type.arguments.isEmpty()) {
            val targetClass = currentElement.getType().toKClass()
            return getSerdesForInterfaceOrConstructorParameters(Class(targetClass))
        }

        // otherwise continue the generic chain
        return type.arguments
            .mapIndexed { genericIndex, typeArgument ->
                typeArgument.type ?: error("Could not resolve generic at index $genericIndex of '${currentElement.getName()}'.")
            }
            .filter { genericType -> !genericType.hasAnnotation<Ignore>() }
            .mapIndexed { genericIndex, genericType ->
                val nextElement = annotationProcessingContext.beginProcessing(Generic(currentElement, genericIndex, genericType))
                serdeFactory.getSerde(nextElement).also {
                    annotationProcessingContext.endProcessingCurrentElement()
                }
            }
    }

    private fun getSerdesForInterfaceOrConstructorParameters(clazz: Class): List<Serde<*>> {

        // resolve serde via annotation on abstract classes or interfaces
        if (clazz.clazz.isAbstract) {
            return listOf(serdeFactory.getSerde(clazz.clazz))
        }

        // resolve serdes by inspecting the primary constructor parameters
        val primaryConstructor = clazz.clazz.primaryConstructor
            ?: error("${clazz.getName()} is required to have a primary constructor.")

        val parameters = primaryConstructor.valueParameters
        return parameters
            .filter { parameter -> !parameter.type.hasAnnotation<Ignore>() }
            .map { parameter ->
                val nextElement = annotationProcessingContext.beginProcessing(ConstructorParameter(parameter))
                serdeFactory.getSerde(nextElement).also {
                    annotationProcessingContext.endProcessingCurrentElement()
                }
            }
    }

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE)
    internal annotation class Ignore
}
