package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import de.darkatra.bfme2.v2.map.deserialization.DeserializerFactory
import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement

internal class DeserializerFactoryResolver(
    private val deserializerFactory: DeserializerFactory
) : ArgumentResolver<DeserializerFactory> {

    override fun resolve(currentElement: ProcessableElement): DeserializerFactory {
        return deserializerFactory
    }
}
