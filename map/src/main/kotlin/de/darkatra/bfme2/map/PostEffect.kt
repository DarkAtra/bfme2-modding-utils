package de.darkatra.bfme2.map

data class PostEffect(
	val name: String,
	val blendFactor: Float?,
	val lookupImage: String?,
	val parameters: List<PostEffectParameter>?
)
