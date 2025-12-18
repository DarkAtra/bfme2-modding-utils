package de.darkatra.bfme2.map.serialization.postprocessing

import de.darkatra.bfme2.map.serialization.SerializationContext
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
internal class NoopPostProcessor<T> : PostProcessor<T> {
    override fun postProcess(data: T, context: SerializationContext) {
        // noop
    }
}
