package de.darkatra.bfme2.map.serialization.postprocessing

import de.darkatra.bfme2.map.serialization.SerializationContext

internal interface PostProcessor<T> {

    fun postProcess(data: T, context: SerializationContext)
}
