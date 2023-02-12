package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.v2.map.deserialization.postprocessing.NoopPostProcessor
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE)
annotation class Deserialize(
    val using: KClass<out Deserializer<*>>,
    val postProcessor: KClass<out PostProcessor<*>> = NoopPostProcessor::class
)
