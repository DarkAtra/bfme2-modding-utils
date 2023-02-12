package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.NoopPostProcessor
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.internal.KotlinReflectionInternalError

class DeserializePostProcessorArgumentResolver : ArgumentResolver<PostProcessor<*>> {

    override fun resolve(deserializationContext: DeserializationContext): PostProcessor<*> {

        val currentElement = deserializationContext.getCurrentElement()

        val deserializeAnnotation = currentElement.getType().findAnnotation<Deserialize>()

        val processorClass = try {
            deserializeAnnotation?.postProcessor
        } catch (e: KotlinReflectionInternalError) {
            // is thrown when accessing the postProcessor attribute for annotations on generics that omit the parameter
            // catch and fall back to the default NoopPostProcessor
            NoopPostProcessor::class
        } ?: NoopPostProcessor::class

        return processorClass.createInstance()
    }
}
