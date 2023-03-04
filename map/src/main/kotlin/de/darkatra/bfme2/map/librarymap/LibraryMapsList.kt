package de.darkatra.bfme2.map.librarymap

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListDeserializer
import de.darkatra.bfme2.map.serialization.Deserialize

@Asset(name = "LibraryMapLists", version = 1u)
data class LibraryMapsList(
    val libraryMaps: @Deserialize(using = AssetListDeserializer::class) List<LibraryMaps>
)
