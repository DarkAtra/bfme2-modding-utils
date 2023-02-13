package de.darkatra.bfme2.v2.map.deserialization.postprocessing

import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
internal annotation class PostProcess(
    val using: KClass<out PostProcessor<*>> = NoopPostProcessor::class
)
