package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.model.ProcessableElement

internal interface ArgumentResolver<T> {

    fun resolve(currentElement: ProcessableElement): T
}
