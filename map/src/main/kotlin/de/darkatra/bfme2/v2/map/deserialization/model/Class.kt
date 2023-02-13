package de.darkatra.bfme2.v2.map.deserialization.model

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
