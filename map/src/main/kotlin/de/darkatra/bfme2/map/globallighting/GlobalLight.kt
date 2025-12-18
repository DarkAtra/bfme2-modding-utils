package de.darkatra.bfme2.map.globallighting

import de.darkatra.bfme2.Vector3
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class GlobalLight(
    val ambient: Vector3,
    val color: Vector3,
    val direction: Vector3
)
