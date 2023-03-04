package de.darkatra.bfme2.map.serialization.postprocessing

import de.darkatra.bfme2.map.serialization.DeserializationContext

internal interface PostProcessor<T> {

    fun postProcess(data: T, context: DeserializationContext)
}
