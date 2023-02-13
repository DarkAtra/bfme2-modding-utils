package de.darkatra.bfme2.v2.map.deserialization

import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE)
internal annotation class Deserialize(
    val using: KClass<out Deserializer<*>>
)
