package de.darkatra.bfme2.map.serialization

import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
internal annotation class UseSerdeProperties(
    val propertiesClass: KClass<*>
)
