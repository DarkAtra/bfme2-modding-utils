package de.darkatra.bfme2.map.librarymap

import de.darkatra.bfme2.map.Asset
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "LibraryMaps", version = 1u)
data class LibraryMaps(
    val names: List<String>
)
