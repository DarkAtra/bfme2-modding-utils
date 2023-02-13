package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement

internal interface ArgumentResolver<T> {

    fun resolve(currentElement: ProcessableElement): T
}
