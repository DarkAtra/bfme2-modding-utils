package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement

internal class DeserializationContextResolver(
    val context: DeserializationContext
) : ArgumentResolver<DeserializationContext> {

    override fun resolve(currentElement: ProcessableElement): DeserializationContext {
        return context
    }
}
