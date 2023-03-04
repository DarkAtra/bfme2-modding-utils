package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.AnnotationProcessingContext
import de.darkatra.bfme2.map.serialization.Deserializer
import de.darkatra.bfme2.map.serialization.DeserializerFactory
import de.darkatra.bfme2.map.serialization.model.ProcessableElement

internal class DeserializerArgumentResolver(
    annotationProcessingContext: AnnotationProcessingContext,
    deserializerFactory: DeserializerFactory
) : ArgumentResolver<Deserializer<*>> {

    private val deserializersArgumentResolver = DeserializersArgumentResolver(annotationProcessingContext, deserializerFactory)

    override fun resolve(currentElement: ProcessableElement): Deserializer<*> {

        val deserializers = deserializersArgumentResolver.resolve(currentElement)

        if (deserializers.size != 1) {
            error("${currentElement.getName()} expects exactly one deserializer but found: $deserializers")
        }

        return deserializers.first()
    }
}
