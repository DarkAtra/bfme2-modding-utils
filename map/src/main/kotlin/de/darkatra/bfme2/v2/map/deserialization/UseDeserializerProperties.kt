package de.darkatra.bfme2.v2.map.deserialization

import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
internal annotation class UseDeserializerProperties(
    val annotation: KClass<*>
)
