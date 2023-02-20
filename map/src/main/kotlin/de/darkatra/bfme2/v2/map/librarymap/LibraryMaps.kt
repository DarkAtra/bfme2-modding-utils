package de.darkatra.bfme2.v2.map.librarymap

import de.darkatra.bfme2.v2.map.Asset

@Asset(name = "LibraryMaps", version = 1u)
data class LibraryMaps(
    val names: List<String>
)
