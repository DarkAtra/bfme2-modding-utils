package de.darkatra.bfme2.map.`object`

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListSerde
import de.darkatra.bfme2.map.serialization.Serialize

@Asset(name = "ObjectsList", version = 3u)
data class Objects(
    val objects: @Serialize(using = AssetListSerde::class) List<MapObject>
)
