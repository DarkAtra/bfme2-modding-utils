package de.darkatra.bfme2.v2.map

@Asset(name = "EnvironmentData", version = 3u)
data class EnvironmentData(
    val waterMaxAlphaDepth: Float,
    val deepWaterAlpha: Float,
    val isMacroTextureStretched: Boolean,
    val macroTexture: String,
    val cloudTexture: String
)
