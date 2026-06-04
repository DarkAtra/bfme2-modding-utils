package de.darkatra.bfme2.w3d.model

import de.darkatra.bfme2.ExperimentalApi

@ExperimentalApi
data class W3dSubChunks(
    @ExperimentalApi
    val children: List<W3dChunk>
) : W3dPayload
