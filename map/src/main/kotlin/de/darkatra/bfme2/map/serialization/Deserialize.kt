package de.darkatra.bfme2.map.serialization

import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
internal annotation class Deserialize(
    val using: KClass<out Deserializer<*>>
)
