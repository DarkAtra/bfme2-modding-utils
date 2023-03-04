package de.darkatra.bfme2.map.librarymap

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListSerde
import de.darkatra.bfme2.map.serialization.Serialize

@Asset(name = "LibraryMapLists", version = 1u)
data class LibraryMapsList(
    val libraryMaps: @Serialize(using = AssetListSerde::class) List<LibraryMaps>
)
