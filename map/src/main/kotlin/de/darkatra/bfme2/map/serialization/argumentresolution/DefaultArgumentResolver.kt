package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.AnnotationProcessingContext
import de.darkatra.bfme2.map.serialization.Serde
import de.darkatra.bfme2.map.serialization.SerdeFactory
import de.darkatra.bfme2.map.serialization.SerializationContext
import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.typeOf

internal class DefaultArgumentResolver(
    private val serdeFactory: SerdeFactory,
    private val serdeClass: KClass<Serde<*>>,
    private val serdeParameter: KParameter
) : ArgumentResolver<Any> {

    private val typeToArgumentResolvers: Map<KType, KClass<out ArgumentResolver<*>>> = mapOf(
        typeOf<AnnotationProcessingContext>() to AnnotationProcessingContextResolver::class,
        typeOf<SerializationContext>() to SerializationContextResolver::class,
        typeOf<Serde<*>>() to SerdeArgumentResolver::class,
        typeOf<List<Serde<*>>>() to SerdesArgumentResolver::class,
        typeOf<PreProcessor<*>>() to PreProcessorArgumentResolver::class,
        typeOf<PostProcessor<*>>() to PostProcessorArgumentResolver::class,
        typeOf<SerdeFactory>() to SerdeFactoryResolver::class,
        typeOf<KClass<*>>() to TypeArgumentResolver::class,
        typeOf<Any>() to SerdePropertiesArgumentResolver::class
    )

    override fun resolve(currentElement: ProcessableElement): Any {

        val argumentResolverClass = getArgumentResolverByType(serdeParameter.type)
            ?: error("No suitable serde found for '${currentElement.getName()}'.")

        val argumentResolver = serdeFactory.getArgumentResolver(argumentResolverClass, serdeClass, serdeParameter)
        return argumentResolver.resolve(currentElement)!!
    }

    private fun getArgumentResolverByType(type: KType): KClass<out ArgumentResolver<*>>? {
        return typeToArgumentResolvers.entries
            .find { (typeToResolve, _) -> typeToResolve.isSupertypeOf(type) }
            ?.value
    }
}
