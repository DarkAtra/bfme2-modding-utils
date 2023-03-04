package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.AnnotationProcessingContext
import de.darkatra.bfme2.map.serialization.model.ProcessableElement

internal class AnnotationProcessingContextResolver(
    private val annotationProcessingContext: AnnotationProcessingContext
) : ArgumentResolver<AnnotationProcessingContext> {

    override fun resolve(currentElement: ProcessableElement): AnnotationProcessingContext {
        return annotationProcessingContext
    }
}
