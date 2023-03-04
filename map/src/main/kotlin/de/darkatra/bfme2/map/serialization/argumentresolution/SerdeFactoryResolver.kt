package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.SerdeFactory
import de.darkatra.bfme2.map.serialization.model.ProcessableElement

internal class SerdeFactoryResolver(
    private val serdeFactory: SerdeFactory
) : ArgumentResolver<SerdeFactory> {

    override fun resolve(currentElement: ProcessableElement): SerdeFactory {
        return serdeFactory
    }
}
