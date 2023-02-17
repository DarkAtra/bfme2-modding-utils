package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import de.darkatra.bfme2.v2.map.deserialization.AnnotationProcessingContext
import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement

internal class AnnotationProcessingContextResolver(
    private val context: AnnotationProcessingContext
) : ArgumentResolver<AnnotationProcessingContext> {

    override fun resolve(currentElement: ProcessableElement): AnnotationProcessingContext {
        return context
    }
}
