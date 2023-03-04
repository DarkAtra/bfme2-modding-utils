package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import de.darkatra.bfme2.map.toKClass
import kotlin.reflect.KClass

internal class TypeArgumentResolver : ArgumentResolver<KClass<*>> {
    override fun resolve(currentElement: ProcessableElement): KClass<*> = currentElement.getType().toKClass()
}
