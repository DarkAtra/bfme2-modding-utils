package de.darkatra.bfme2.map.serialization.argumentresolution

import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
internal annotation class Resolve(
    val using: KClass<out ArgumentResolver<*>>
)
