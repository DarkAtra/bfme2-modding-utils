package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.Deserializer
import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.typeOf

internal class DefaultArgumentResolver(
    private val context: DeserializationContext,
    private val deserializerClass: KClass<Deserializer<*>>,
    private val deserializerParameter: KParameter
) : ArgumentResolver<Any> {

    private val typeToArgumentResolvers: Map<KType, KClass<out ArgumentResolver<*>>> = mapOf(
        typeOf<DeserializationContext>() to DeserializationContextResolver::class,
        typeOf<Deserializer<*>>() to DeserializerArgumentResolver::class,
        typeOf<List<Deserializer<*>>>() to DeserializersArgumentResolver::class,
        typeOf<PostProcessor<*>>() to PostProcessorArgumentResolver::class,
        typeOf<KClass<*>>() to TypeArgumentResolver::class,
        typeOf<Any>() to AnnotationParameterArgumentResolver::class
    )

    override fun resolve(currentElement: ProcessableElement): Any {

        val argumentResolverClass = getArgumentResolverByType(deserializerParameter.type)
            ?: error("No suitable deserializer found for '${currentElement.getName()}'.")

        val argumentResolver = context.deserializerFactory.getArgumentResolver(argumentResolverClass, deserializerClass, deserializerParameter)
        return argumentResolver.resolve(currentElement)!!
    }

    private fun getArgumentResolverByType(type: KType): KClass<out ArgumentResolver<*>>? {
        return typeToArgumentResolvers.entries
            .find { (typeToResolve, _) -> typeToResolve.isSupertypeOf(type) }
            ?.value
    }
}
