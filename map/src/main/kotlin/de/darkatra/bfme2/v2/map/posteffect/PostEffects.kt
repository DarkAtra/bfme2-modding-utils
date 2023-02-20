package de.darkatra.bfme2.v2.map.posteffect

import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer.SizeType

@Asset(name = "PostEffectsChunk", version = 1u)
data class PostEffects(
    val postEffects: @ListDeserializer.Properties(sizeType = SizeType.BYTE) List<PostEffect>
)
