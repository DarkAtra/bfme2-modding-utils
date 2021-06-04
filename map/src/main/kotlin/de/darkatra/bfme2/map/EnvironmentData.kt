package de.darkatra.bfme2.map

data class EnvironmentData(
	val waterMaxAlphaDepth: Float?,
	val deepWaterAlpha: Float?,
	val isMacroTextureStretched: Boolean,
	val macroTexture: String,
	val cloudTexture: String,
	// TODO: what are these textures used for?
	val unknownTexture: String?,
	val unknownTexture2: String?
)
