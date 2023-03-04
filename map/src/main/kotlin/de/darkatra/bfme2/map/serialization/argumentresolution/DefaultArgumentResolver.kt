package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.AnnotationProcessingContext
import de.darkatra.bfme2.map.serialization.DeserializationContext
import de.darkatra.bfme2.map.serialization.Deserializer
import de.darkatra.bfme2.map.serialization.DeserializerFactory
import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.typeOf

internal class DefaultArgumentResolver(
    private val deserializerFactory: DeserializerFactory,
    private val deserializerClass: KClass<Deserializer<*>>,
    private val deserializerParameter: KParameter
) : ArgumentResolver<Any> {

    private val typeToArgumentResolvers: Map<KType, KClass<out ArgumentResolver<*>>> = mapOf(
        typeOf<AnnotationProcessingContext>() to AnnotationProcessingContextResolver::class,
        typeOf<DeserializationContext>() to DeserializationContextResolver::class,
        typeOf<Deserializer<*>>() to DeserializerArgumentResolver::class,
        typeOf<List<Deserializer<*>>>() to DeserializersArgumentResolver::class,
        typeOf<PostProcessor<*>>() to PostProcessorArgumentResolver::class,
        typeOf<DeserializerFactory>() to DeserializerFactoryResolver::class,
        typeOf<KClass<*>>() to TypeArgumentResolver::class,
        typeOf<Any>() to AnnotationParameterArgumentResolver::class
    )

    override fun resolve(currentElement: ProcessableElement): Any {

        val argumentResolverClass = getArgumentResolverByType(deserializerParameter.type)
            ?: error("No suitable deserializer found for '${currentElement.getName()}'.")

        val argumentResolver = deserializerFactory.getArgumentResolver(argumentResolverClass, deserializerClass, deserializerParameter)
        return argumentResolver.resolve(currentElement)!!
    }

    private fun getArgumentResolverByType(type: KType): KClass<out ArgumentResolver<*>>? {
        return typeToArgumentResolvers.entries
            .find { (typeToResolve, _) -> typeToResolve.isSupertypeOf(type) }
            ?.value
    }
}
