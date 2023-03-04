package de.darkatra.bfme2.map.serialization.postprocessing

import de.darkatra.bfme2.map.serialization.DeserializationContext

internal abstract class SharedDataProvidingPostProcessor<T : Any>(
    private val sharedDataKey: String
) : PostProcessor<T> {
    override fun postProcess(data: T, context: DeserializationContext) {
        context.sharedData[sharedDataKey] = data
    }
}
