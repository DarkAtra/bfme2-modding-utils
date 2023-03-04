package de.darkatra.bfme2.map.posteffect

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.ListDeserializer
import de.darkatra.bfme2.map.serialization.ListDeserializer.SizeType

@Asset(name = "PostEffectsChunk", version = 1u)
data class PostEffects(
    val postEffects: @ListDeserializer.Properties(sizeType = SizeType.BYTE) List<PostEffect>
)
