package de.darkatra.bfme2.v2.map.deserialization.postprocessing

import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext

class NoopPostProcessor<T> : PostProcessor<T> {
    override fun postProcess(data: T, deserializationContext: DeserializationContext) {
        // noop
    }
}
