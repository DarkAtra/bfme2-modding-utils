package de.darkatra.bfme2.w3d

import de.darkatra.bfme2.w3d.model.W3dBox
import de.darkatra.bfme2.w3d.model.W3dChunkType
import de.darkatra.bfme2.w3d.model.W3dSubChunks
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class W3dFileReaderTest {

    private val w3dFileReader = W3dFileReader()

    @Test
    fun `shoud read w3d file`() {

        val w3dFile = TestUtils.getInputStream("/models/gaduz.w3d").use(w3dFileReader::read)

        assertThat(w3dFile.chunks).hasSize(3)
        assertThat(w3dFile.chunks[0].type).isEqualTo(W3dChunkType.W3D_CHUNK_MESH)
        assertThat(w3dFile.chunks[0].payload).isInstanceOf(W3dSubChunks::class.java)
        assertThat(w3dFile.chunks[0].start).isEqualTo(0u)
        assertThat(w3dFile.chunks[0].end).isEqualTo(60091u)
        assertThat(w3dFile.chunks[1].type).isEqualTo(W3dChunkType.W3D_CHUNK_MESH)
        assertThat(w3dFile.chunks[1].payload).isInstanceOf(W3dSubChunks::class.java)
        assertThat(w3dFile.chunks[1].start).isEqualTo(60091u)
        assertThat(w3dFile.chunks[1].end).isEqualTo(79927u)
        assertThat(w3dFile.chunks[2].type).isEqualTo(W3dChunkType.W3D_CHUNK_HLOD)
        assertThat(w3dFile.chunks[2].payload).isInstanceOf(W3dSubChunks::class.java)
        assertThat(w3dFile.chunks[2].start).isEqualTo(79927u)
        assertThat(w3dFile.chunks[2].end).isEqualTo(80095u)

        val subChunk1 = w3dFile.chunks[0].payload as W3dSubChunks
        assertThat(subChunk1.children).hasSize(13)
    }

    @Test
    fun `shoud read chunk start and end correctly`() {

        val w3dFile = TestUtils.getInputStream("/models/guaragorn_skn.w3d").use(w3dFileReader::read)

        assertThat(w3dFile.chunks).hasSize(4)
        assertThat(w3dFile.chunks[0].type).isEqualTo(W3dChunkType.W3D_CHUNK_MESH)
        assertThat(w3dFile.chunks[0].payload).isInstanceOf(W3dSubChunks::class.java)
        assertThat(w3dFile.chunks[0].start).isEqualTo(0u)
        assertThat(w3dFile.chunks[0].end).isEqualTo(47356u)
        assertThat(w3dFile.chunks[1].type).isEqualTo(W3dChunkType.W3D_CHUNK_MESH)
        assertThat(w3dFile.chunks[1].payload).isInstanceOf(W3dSubChunks::class.java)
        assertThat(w3dFile.chunks[1].start).isEqualTo(47356u)
        assertThat(w3dFile.chunks[1].end).isEqualTo(49294u)
        assertThat(w3dFile.chunks[2].type).isEqualTo(W3dChunkType.W3D_CHUNK_BOX)
        assertThat(w3dFile.chunks[2].payload).isInstanceOf(W3dBox::class.java)
        assertThat(w3dFile.chunks[2].start).isEqualTo(49294u)
        assertThat(w3dFile.chunks[2].end).isEqualTo(49370u)
        assertThat(w3dFile.chunks[3].type).isEqualTo(W3dChunkType.W3D_CHUNK_HLOD)
        assertThat(w3dFile.chunks[3].payload).isInstanceOf(W3dSubChunks::class.java)
        assertThat(w3dFile.chunks[3].start).isEqualTo(49370u)
        assertThat(w3dFile.chunks[3].end).isEqualTo(49582u)
    }
}
