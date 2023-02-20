package de.darkatra.bfme2.v2.map.`object`

import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.deserialization.AssetListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize

@Asset(name = "ObjectsList", version = 3u)
data class Objects(
    val objects: @Deserialize(using = AssetListDeserializer::class) List<MapObject>
)
