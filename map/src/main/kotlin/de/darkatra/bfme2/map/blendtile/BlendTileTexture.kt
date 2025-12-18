package de.darkatra.bfme2.map.blendtile

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.serialization.SerializationContext
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcess
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@PostProcess(using = BlendTileTexture.BlendTileTexturePostProcessor::class)
data class BlendTileTexture(
    val cellStart: UInt,
    val cellCount: UInt,
    val cellSize: UInt,
    val magicValue: UInt,
    val name: String
) {

    @ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
    internal class BlendTileTexturePostProcessor : PostProcessor<BlendTileTexture> {
        override fun postProcess(data: BlendTileTexture, context: SerializationContext) {
            if (data.cellSize * data.cellSize != data.cellCount) {
                throw InvalidDataException("Expected cellCount '${data.cellCount}' to equal cellSize times cellSize (${data.cellSize} * ${data.cellSize}).")
            }
            if (data.magicValue != 0u) {
                throw InvalidDataException("Expected magicValue to be zero.")
            }
        }
    }
}
