package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.DeserializerFactory
import de.darkatra.bfme2.map.serialization.model.ProcessableElement

internal class DeserializerFactoryResolver(
    private val deserializerFactory: DeserializerFactory
) : ArgumentResolver<DeserializerFactory> {

    override fun resolve(currentElement: ProcessableElement): DeserializerFactory {
        return deserializerFactory
    }
}
