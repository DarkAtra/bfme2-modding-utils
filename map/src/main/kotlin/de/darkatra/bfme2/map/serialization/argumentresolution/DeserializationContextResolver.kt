package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.DeserializationContext
import de.darkatra.bfme2.map.serialization.model.ProcessableElement

internal class DeserializationContextResolver(
    private val context: DeserializationContext
) : ArgumentResolver<DeserializationContext> {

    override fun resolve(currentElement: ProcessableElement): DeserializationContext {
        return context
    }
}
