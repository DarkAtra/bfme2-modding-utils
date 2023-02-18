package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.ListDeserializer.SizeType

@Asset(name = "PostEffectsChunk", version = 1u)
data class PostEffects(
    val postEffects: @ListDeserializer.Properties(sizeType = SizeType.BYTE) List<PostEffect>
) {

    data class PostEffect(
        val name: String,
        val blendFactor: Float,
        val lookupImage: String
    )
}
