package de.darkatra.bfme2.map

import kotlin.reflect.KClass
import kotlin.reflect.KType

fun KType.toKClass(): KClass<*> {
    val classOfT = classifier
    if (classOfT !is KClass<*>) {
        error("Could not get KClass for KType '${this}'.")
    }
    return classOfT
}
