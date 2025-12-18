package de.darkatra.bfme2.map.serialization.preprocessing

import de.darkatra.bfme2.map.serialization.SerializationContext
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
internal class NoopPreProcessor<T> : PreProcessor<T> {
    override fun preProcess(data: T, context: SerializationContext): T {
        return data
    }
}
