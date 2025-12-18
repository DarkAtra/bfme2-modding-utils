package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.AnnotationProcessingContext
import de.darkatra.bfme2.map.serialization.Serde
import de.darkatra.bfme2.map.serialization.SerdeFactory
import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
internal class SerdeArgumentResolver(
    annotationProcessingContext: AnnotationProcessingContext,
    serdeFactory: SerdeFactory
) : ArgumentResolver<Serde<*>> {

    private val serdesArgumentResolver = SerdesArgumentResolver(annotationProcessingContext, serdeFactory)

    override fun resolve(currentElement: ProcessableElement): Serde<*> {

        val serdes = serdesArgumentResolver.resolve(currentElement)

        if (serdes.size != 1) {
            error("${currentElement.getName()} expects exactly one serde but found: $serdes")
        }

        return serdes.first()
    }
}
