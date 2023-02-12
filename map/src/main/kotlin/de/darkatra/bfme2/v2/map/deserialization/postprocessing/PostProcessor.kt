package de.darkatra.bfme2.v2.map.deserialization.postprocessing

import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext

interface PostProcessor<T> {

    fun postProcess(data: T, deserializationContext: DeserializationContext)
}
