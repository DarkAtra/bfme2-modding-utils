package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import kotlin.reflect.KClass

// TODO: implement SmartArgumentResolver that infers which ArgumentResolver should be used based on the parameter type
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
internal annotation class Resolve(
    val using: KClass<out ArgumentResolver<*>>
)
