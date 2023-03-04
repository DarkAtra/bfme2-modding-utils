package de.darkatra.bfme2.map.serialization.model

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

internal class Class(
    internal val clazz: KClass<*>
) : ProcessableElement {

    override fun getName(): String {
        return clazz.simpleName!!
    }

    override fun getType(): KType {
        return clazz.starProjectedType
    }
}
