package de.darkatra.bfme2.map.environment

import de.darkatra.bfme2.map.Asset
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "EnvironmentData", version = 3u)
data class EnvironmentData(
    val waterMaxAlphaDepth: Float,
    val deepWaterAlpha: Float,
    val isMacroTextureStretched: Boolean,
    val macroTexture: String,
    val cloudTexture: String
)
