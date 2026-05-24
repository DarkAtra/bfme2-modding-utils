package de.darkatra.bfme2.w3d.model

import com.google.common.io.CountingInputStream

data class W3dChunk(
    val type: W3dChunkType,
    val payload: W3dPayload,
) {

    internal companion object {

        internal fun read(countingInputStream: CountingInputStream): W3dChunk {

            val chunkHeader = W3dChunkHeader.read(countingInputStream)

            val payload: W3dPayload = when (chunkHeader.type) {
                W3dChunkType.W3D_CHUNK_MESH_HEADER3 -> W3dMeshHeader.read(countingInputStream)
                W3dChunkType.W3D_CHUNK_MATERIAL_INFO -> W3dMaterialInfo.read(countingInputStream)
                W3dChunkType.W3D_CHUNK_VERTEX_MATERIAL_NAME -> W3dVertexMaterialName.read(countingInputStream, chunkHeader.size)
                W3dChunkType.W3D_CHUNK_TEXTURE_NAME -> W3dTextureName.read(countingInputStream, chunkHeader.size)
                W3dChunkType.W3D_CHUNK_HLOD_HEADER -> W3dHLodHeader.read(countingInputStream)
                W3dChunkType.W3D_CHUNK_HLOD_SUB_OBJECT_ARRAY_HEADER -> W3dHLodSubObjectArrayHeader.read(countingInputStream)
                W3dChunkType.W3D_CHUNK_HLOD_SUB_OBJECT -> W3dHLodSubObject.read(countingInputStream)
                W3dChunkType.W3D_CHUNK_BOX -> W3dBox.read(countingInputStream)
                else -> when (chunkHeader.hasSubChunks) {
                    false -> W3dRawPayload.read(countingInputStream, chunkHeader.size)
                    true -> W3dSubChunks(
                        children = buildList {
                            while (countingInputStream.count.toULong() < chunkHeader.end) {
                                add(read(countingInputStream))
                            }
                        }
                    )
                }
            }

            return W3dChunk(
                type = chunkHeader.type,
                payload = payload,
            )
        }
    }
}
