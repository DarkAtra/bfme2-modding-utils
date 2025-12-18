package de.darkatra.bfme2.map.librarymap

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListSerde
import de.darkatra.bfme2.map.serialization.Serialize
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "LibraryMapLists", version = 1u)
data class LibraryMapsList(
    val libraryMaps: @Serialize(using = AssetListSerde::class) List<LibraryMaps>
)
