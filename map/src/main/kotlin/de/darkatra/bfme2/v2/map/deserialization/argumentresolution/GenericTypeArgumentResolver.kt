package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.Deserializer
import de.darkatra.bfme2.v2.map.deserialization.model.GenericType

abstract class GenericTypeArgumentResolver {

    protected fun resolveInternal(deserializationContext: DeserializationContext, genericIndex: Int): Deserializer<*> {

        val genericType = deserializationContext.getCurrentElement().getType().arguments[genericIndex].type
            ?: error("Could not resolve generic at index $genericIndex of '${deserializationContext.getCurrentElement().getName()}'.")

        deserializationContext.beginProcessing(
            GenericType(deserializationContext.getCurrentElement(), genericIndex, genericType)
        )
        return deserializationContext.deserializerFactory.getDeserializer(deserializationContext).also {
            deserializationContext.endProcessingCurrentElement()
        }
    }
}
