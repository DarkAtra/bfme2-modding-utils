package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import de.darkatra.bfme2.map.serialization.postprocessing.NoopPostProcessor
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcess
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.toKClass
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.internal.KotlinReflectionInternalError

internal class PostProcessorArgumentResolver : ArgumentResolver<PostProcessor<*>> {

    override fun resolve(currentElement: ProcessableElement): PostProcessor<*> {

        val postProcessorClass = getPostProcessorClassFromAnnotation(currentElement)
            ?: currentElement.getType().toKClass().findAnnotation<PostProcess>()?.using
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
}
