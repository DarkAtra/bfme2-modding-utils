package de.darkatra.bfme2.map.`object`

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListDeserializer
import de.darkatra.bfme2.map.serialization.Deserialize

@Asset(name = "ObjectsList", version = 3u)
data class Objects(
    val objects: @Deserialize(using = AssetListDeserializer::class) List<MapObject>
)
