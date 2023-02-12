package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Resolve(
    val using: KClass<out ArgumentResolver<*>>
)
