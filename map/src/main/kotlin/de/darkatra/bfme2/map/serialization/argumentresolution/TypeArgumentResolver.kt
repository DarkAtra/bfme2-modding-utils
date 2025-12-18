package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import de.darkatra.bfme2.map.toKClass
import io.goodforgod.graalvm.hint.annotation.ReflectionHint
import kotlin.reflect.KClass

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
internal class TypeArgumentResolver : ArgumentResolver<KClass<*>> {
    override fun resolve(currentElement: ProcessableElement): KClass<*> = currentElement.getType().toKClass()
}
