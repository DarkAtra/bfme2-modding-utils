package de.darkatra.bfme2.v2.map.deserialization.postprocessing

import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext

internal class LoggingPostProcessor<T> : PostProcessor<T> {
    override fun postProcess(data: T, context: DeserializationContext) {
        println(data)
    }
}
