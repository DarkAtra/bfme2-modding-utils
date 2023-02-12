package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import kotlin.reflect.KClass

class TypeArgumentResolver : ArgumentResolver<KClass<*>> {

    override fun resolve(deserializationContext: DeserializationContext): KClass<*> {

        val classOfT = deserializationContext.getCurrentElement().getType().classifier
        if (classOfT !is KClass<*>) {
            error("${TypeArgumentResolver::class.simpleName} only supports resolving types for classes.")
        }

        return classOfT
    }
}
