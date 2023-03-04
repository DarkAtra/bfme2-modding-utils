package de.darkatra.bfme2.map.serialization.postprocessing

import de.darkatra.bfme2.map.serialization.DeserializationContext

@Suppress("unused") // for debugging purposes
internal class LoggingPostProcessor<T> : PostProcessor<T> {
    override fun postProcess(data: T, context: DeserializationContext) {
        println(data)
    }
}
