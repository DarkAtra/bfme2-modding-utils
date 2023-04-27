package de.darkatra.bfme2.map.serialization.preprocessing

import de.darkatra.bfme2.map.serialization.SerializationContext

internal class NoopPreProcessor<T> : PreProcessor<T> {
    override fun preProcess(data: T, context: SerializationContext): T {
        return data
    }
}
