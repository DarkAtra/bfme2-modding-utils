package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import kotlin.reflect.KClass

internal class TypeArgumentResolver : ArgumentResolver<KClass<*>> {

    override fun resolve(currentElement: ProcessableElement): KClass<*> {

        val classOfT = currentElement.getType().classifier
        if (classOfT !is KClass<*>) {
            error("${TypeArgumentResolver::class.simpleName} only supports resolving types for classes.")
        }

        return classOfT
    }
}
