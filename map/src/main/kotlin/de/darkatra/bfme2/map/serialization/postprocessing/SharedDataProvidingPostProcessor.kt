package de.darkatra.bfme2.map.serialization.postprocessing

import de.darkatra.bfme2.map.serialization.SerializationContext

internal abstract class SharedDataProvidingPostProcessor<T : Any>(
    private val sharedDataKey: String
) : PostProcessor<T> {
    override fun postProcess(data: T, context: SerializationContext) {
        context.sharedData[sharedDataKey] = data
    }
}
