package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import de.darkatra.bfme2.map.serialization.preprocessing.NoopPreProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcess
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.map.toKClass
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.internal.KotlinReflectionInternalError

internal class PreProcessorArgumentResolver : ArgumentResolver<PreProcessor<*>> {

    override fun resolve(currentElement: ProcessableElement): PreProcessor<*> {

        val preProcessorClass = getPreProcessorClassFromAnnotation(currentElement)
            ?: currentElement.getType().toKClass().findAnnotation<PreProcess>()?.using
            ?: NoopPreProcessor::class

        return preProcessorClass.createInstance()
    }

    private fun getPreProcessorClassFromAnnotation(currentElement: ProcessableElement): KClass<out PreProcessor<*>>? {

        val preProcessAnnotation = currentElement.getType().findAnnotation<PreProcess>()
        return try {
            preProcessAnnotation?.using
        } catch (e: KotlinReflectionInternalError) {
            // is thrown when accessing the preProcessor attribute for annotations on generics that omit the parameter
            // null means we were unable to resolve a specific post processor
            null
        }
    }
}
