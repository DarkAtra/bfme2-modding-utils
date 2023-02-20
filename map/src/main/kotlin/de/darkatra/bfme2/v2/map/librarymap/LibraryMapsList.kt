package de.darkatra.bfme2.v2.map.librarymap

import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.deserialization.AssetListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize

@Asset(name = "LibraryMapLists", version = 1u)
data class LibraryMapsList(
    val libraryMaps: @Deserialize(using = AssetListDeserializer::class) List<LibraryMaps>
)
