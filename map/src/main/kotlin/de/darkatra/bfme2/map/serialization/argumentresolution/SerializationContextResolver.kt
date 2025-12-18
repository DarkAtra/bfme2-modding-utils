package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.SerializationContext
import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
internal class SerializationContextResolver(
    private val serializationContext: SerializationContext
) : ArgumentResolver<SerializationContext> {

    override fun resolve(currentElement: ProcessableElement): SerializationContext {
        return serializationContext
    }
}
