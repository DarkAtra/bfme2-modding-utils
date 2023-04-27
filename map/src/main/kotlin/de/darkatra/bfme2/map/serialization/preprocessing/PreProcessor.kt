package de.darkatra.bfme2.map.serialization.preprocessing

import de.darkatra.bfme2.map.serialization.SerializationContext

internal interface PreProcessor<T> {

    fun preProcess(data: T, context: SerializationContext): T
}
