package de.darkatra.bfme2.map.blendtile

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.serialization.SerializationContext
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcess
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor

@PostProcess(using = BlendTileTexture.BlendTileTexturePostProcessor::class)
data class BlendTileTexture(
    val cellStart: UInt,
    val cellCount: UInt,
    val cellSize: UInt,
    val magicValue: UInt,
    val name: String
) {

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
