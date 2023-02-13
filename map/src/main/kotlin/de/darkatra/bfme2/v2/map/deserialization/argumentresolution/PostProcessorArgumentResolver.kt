package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.NoopPostProcessor
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcess
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.internal.KotlinReflectionInternalError

internal class PostProcessorArgumentResolver : ArgumentResolver<PostProcessor<*>> {

    private val typeArgumentResolver = TypeArgumentResolver()

    override fun resolve(currentElement: ProcessableElement): PostProcessor<*> {

        val postProcessorClass = getPostProcessorClassFromAnnotation(currentElement)
            ?: getPostProcessorClassFromTargetClass(currentElement)
            ?: NoopPostProcessor::class

        return postProcessorClass.createInstance()
    }

    private fun getPostProcessorClassFromAnnotation(currentElement: ProcessableElement): KClass<out PostProcessor<*>>? {

        val postProcessAnnotation = currentElement.getType().findAnnotation<PostProcess>()
        return try {
            postProcessAnnotation?.using
        } catch (e: KotlinReflectionInternalError) {
            // is thrown when accessing the postProcessor attribute for annotations on generics that omit the parameter
            // null means we were unable to resolve a specific post processor
            null
        }
    }

    private fun getPostProcessorClassFromTargetClass(currentElement: ProcessableElement): KClass<out PostProcessor<*>>? {
        val targetClass = typeArgumentResolver.resolve(currentElement)
        return targetClass.findAnnotation<PostProcess>()?.using
    }
}
