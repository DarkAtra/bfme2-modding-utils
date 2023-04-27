package de.darkatra.bfme2.map.serialization.postprocessing

import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
internal annotation class PostProcess(
    val using: KClass<out PostProcessor<*>> = NoopPostProcessor::class
)
