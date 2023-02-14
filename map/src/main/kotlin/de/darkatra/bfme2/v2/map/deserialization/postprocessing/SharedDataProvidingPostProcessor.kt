package de.darkatra.bfme2.v2.map.deserialization.postprocessing

import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext

internal abstract class SharedDataProvidingPostProcessor<T : Any>(
    private val sharedDataKey: String
) : PostProcessor<T> {
    override fun postProcess(data: T, context: DeserializationContext) {
        context.sharedData[sharedDataKey] = data
    }
}
