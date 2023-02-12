package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext

interface ArgumentResolver<T> {

    fun resolve(deserializationContext: DeserializationContext): T
}
