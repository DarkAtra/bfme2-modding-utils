package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.AnnotationProcessingContext
import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
internal class AnnotationProcessingContextResolver(
    private val annotationProcessingContext: AnnotationProcessingContext
) : ArgumentResolver<AnnotationProcessingContext> {

    override fun resolve(currentElement: ProcessableElement): AnnotationProcessingContext {
        return annotationProcessingContext
    }
}
