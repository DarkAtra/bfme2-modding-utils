package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.ExperimentalApi

@ExperimentalApi
data class W3dFile(
    @ExperimentalApi
    val chunks: List<W3dChunk>,
)
