package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.deserialization.AssetListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.UShortPrefixedStringDeserializer

@Asset(name = "LibraryMapLists", version = 1u)
data class LibraryMapsList(
    val libraryMaps: @Deserialize(using = AssetListDeserializer::class) List<LibraryMaps>
) {

    @Asset(name = "LibraryMaps", version = 1u)
    data class LibraryMaps(
        val names: List<@Deserialize(using = UShortPrefixedStringDeserializer::class) String>
    )
}
