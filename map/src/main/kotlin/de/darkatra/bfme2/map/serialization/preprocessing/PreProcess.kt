package de.darkatra.bfme2.map.serialization.preprocessing

import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
internal annotation class PreProcess(
    val using: KClass<out PreProcessor<*>>
)
