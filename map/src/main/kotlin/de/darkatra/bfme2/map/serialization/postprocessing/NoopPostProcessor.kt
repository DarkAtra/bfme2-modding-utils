package de.darkatra.bfme2.map.serialization.postprocessing

import de.darkatra.bfme2.map.serialization.DeserializationContext

internal class NoopPostProcessor<T> : PostProcessor<T> {
    override fun postProcess(data: T, context: DeserializationContext) {
        // noop
    }
}
