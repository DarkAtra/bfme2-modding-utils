package de.darkatra.bfme2.map.serialization.postprocessing

import de.darkatra.bfme2.map.serialization.SerializationContext

internal class NoopPostProcessor<T> : PostProcessor<T> {
    override fun postProcess(data: T, context: SerializationContext) {
        // noop
    }
}
