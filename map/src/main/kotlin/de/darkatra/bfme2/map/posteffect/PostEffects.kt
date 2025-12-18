package de.darkatra.bfme2.map.posteffect

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.ListSerde
import de.darkatra.bfme2.map.serialization.ListSerde.SizeType
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "PostEffectsChunk", version = 1u)
data class PostEffects(
    val postEffects: @ListSerde.Properties(sizeType = SizeType.BYTE) List<PostEffect>
)
