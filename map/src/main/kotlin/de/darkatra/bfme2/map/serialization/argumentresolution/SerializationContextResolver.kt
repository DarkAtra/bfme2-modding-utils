package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.SerializationContext
import de.darkatra.bfme2.map.serialization.model.ProcessableElement

internal class SerializationContextResolver(
    private val serializationContext: SerializationContext
) : ArgumentResolver<SerializationContext> {

    override fun resolve(currentElement: ProcessableElement): SerializationContext {
        return serializationContext
    }
}
