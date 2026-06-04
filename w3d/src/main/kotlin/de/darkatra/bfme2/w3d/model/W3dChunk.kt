package de.darkatra.bfme2.w3d.model

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.ExperimentalApi

@ExperimentalApi
data class W3dChunk(
    @ExperimentalApi
    val type: W3dChunkType,
    @ExperimentalApi
    val payload: W3dPayload,
    @ExperimentalApi
    val start: UInt,
    @ExperimentalApi
    val end: UInt,
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
                W3dChunkType.W3D_CHUNK_HIERARCHY_HEADER -> W3dHierarchyHeader.read(countingInputStream)
                W3dChunkType.W3D_CHUNK_ANIMATION_HEADER -> W3dAnimationHeader.read(countingInputStream)
                W3dChunkType.W3D_CHUNK_COMPRESSED_ANIMATION_HEADER -> W3dCompressedAnimationHeader.read(countingInputStream)
                W3dChunkType.W3D_CHUNK_BOX -> W3dBox.read(countingInputStream)
                W3dChunkType.W3D_CHUNK_VERTEX_MAPPER_ARGS0,
                W3dChunkType.W3D_CHUNK_VERTEX_MAPPER_ARGS1 -> W3dRawPayload.read(countingInputStream, chunkHeader.size)

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
                start = chunkHeader.start,
                end = chunkHeader.end,
            )
        }
    }
}
