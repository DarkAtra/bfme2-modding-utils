package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.Deserializer

class MapValueArgumentResolver : GenericTypeArgumentResolver(), ArgumentResolver<Deserializer<*>> {

    override fun resolve(deserializationContext: DeserializationContext): Deserializer<*> {
        return resolveInternal(deserializationContext, 1)
    }
}
