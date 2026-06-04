@file:OptIn(ExperimentalApi::class)

package de.darkatra.bfme2.w3d

import de.darkatra.bfme2.ExperimentalApi
import de.darkatra.bfme2.w3d.model.W3dAnimationHeader
import de.darkatra.bfme2.w3d.model.W3dBox
import de.darkatra.bfme2.w3d.model.W3dChunkType
import de.darkatra.bfme2.w3d.model.W3dCompressedAnimationHeader
import de.darkatra.bfme2.w3d.model.W3dHierarchyHeader
import de.darkatra.bfme2.w3d.model.W3dSubChunks
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class W3dFileReaderTest {

    private val w3dFileReader = W3dFileReader()

    @Test
    fun `should read w3d file`() {

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
    fun `should read chunk start and end correctly`() {

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

    @Test
    fun `should read hierarchy chunk correctly`() {

        val w3dFile = TestUtils.getInputStream("/models/guaragorn_skl.w3d").use(w3dFileReader::read)

        assertThat(w3dFile.chunks).hasSize(1)
        assertThat(w3dFile.chunks[0].type).isEqualTo(W3dChunkType.W3D_CHUNK_HIERARCHY)
        assertThat(w3dFile.chunks[0].payload).isInstanceOf(W3dSubChunks::class.java)
        assertThat(w3dFile.chunks[0].start).isEqualTo(0u)
        assertThat(w3dFile.chunks[0].end).isEqualTo(1860u)

        val subChunks = w3dFile.chunks[0].payload as W3dSubChunks
        assertThat(subChunks.children).hasSize(2)
        assertThat(subChunks.children[0].type).isEqualTo(W3dChunkType.W3D_CHUNK_HIERARCHY_HEADER)
        assertThat(subChunks.children[0].payload).isInstanceOf(W3dHierarchyHeader::class.java)
        assertThat((subChunks.children[0].payload as W3dHierarchyHeader).name).isEqualTo("GUARAGORN_SKL")
        assertThat(subChunks.children[1].type).isEqualTo(W3dChunkType.W3D_CHUNK_PIVOTS)
    }

    @Test
    fun `should read hierarchy chunk without subchunks correctly`() {

        val w3dFile = TestUtils.getInputStream("/models/iuwargarch_skl.w3d").use(w3dFileReader::read)

        assertThat(w3dFile.chunks).hasSize(1)
        assertThat(w3dFile.chunks[0].type).isEqualTo(W3dChunkType.W3D_CHUNK_HIERARCHY)
        assertThat(w3dFile.chunks[0].payload).isInstanceOf(W3dSubChunks::class.java)
        assertThat(w3dFile.chunks[0].start).isEqualTo(0u)
        assertThat(w3dFile.chunks[0].end).isEqualTo(3120u)

        val subChunks = w3dFile.chunks[0].payload as W3dSubChunks
        assertThat(subChunks.children).hasSize(2)
        assertThat(subChunks.children[0].type).isEqualTo(W3dChunkType.W3D_CHUNK_HIERARCHY_HEADER)
        assertThat(subChunks.children[0].payload).isInstanceOf(W3dHierarchyHeader::class.java)
        assertThat((subChunks.children[0].payload as W3dHierarchyHeader).name).isEqualTo("IUWARGARCH_SKL")
        assertThat(subChunks.children[1].type).isEqualTo(W3dChunkType.W3D_CHUNK_PIVOTS)
    }

    @Test
    fun `should read animation chunk correctly`() {

        val w3dFile = TestUtils.getInputStream("/models/guaragorn_pala.w3d").use(w3dFileReader::read)

        assertThat(w3dFile.chunks).hasSize(1)
        assertThat(w3dFile.chunks[0].type).isEqualTo(W3dChunkType.W3D_CHUNK_ANIMATION)
        assertThat(w3dFile.chunks[0].payload).isInstanceOf(W3dSubChunks::class.java)
        assertThat(w3dFile.chunks[0].start).isEqualTo(0u)
        assertThat(w3dFile.chunks[0].end).isEqualTo(126540u)

        val subChunks = w3dFile.chunks[0].payload as W3dSubChunks
        assertThat(subChunks.children).hasSize(81)
        assertThat(subChunks.children[0].type).isEqualTo(W3dChunkType.W3D_CHUNK_ANIMATION_HEADER)
        assertThat(subChunks.children[0].payload).isInstanceOf(W3dAnimationHeader::class.java)
        assertThat((subChunks.children[0].payload as W3dAnimationHeader).name).isEqualTo("GUARAGORN_PALA")
        assertThat((subChunks.children[0].payload as W3dAnimationHeader).hierarchyName).isEqualTo("GUISILDUR_SKL")
    }

    @Test
    fun `should read compressed animation chunk correctly`() {

        val w3dFile = TestUtils.getInputStream("/models/guaragorn_idla.w3d").use(w3dFileReader::read)

        assertThat(w3dFile.chunks).hasSize(1)
        assertThat(w3dFile.chunks[0].type).isEqualTo(W3dChunkType.W3D_CHUNK_COMPRESSED_ANIMATION)
        assertThat(w3dFile.chunks[0].payload).isInstanceOf(W3dSubChunks::class.java)
        assertThat(w3dFile.chunks[0].start).isEqualTo(0u)
        assertThat(w3dFile.chunks[0].end).isEqualTo(12183u)

        val subChunks = w3dFile.chunks[0].payload as W3dSubChunks
        assertThat(subChunks.children).hasSize(60)
        assertThat(subChunks.children[0].type).isEqualTo(W3dChunkType.W3D_CHUNK_COMPRESSED_ANIMATION_HEADER)
        assertThat(subChunks.children[0].payload).isInstanceOf(W3dCompressedAnimationHeader::class.java)
        assertThat((subChunks.children[0].payload as W3dCompressedAnimationHeader).name).isEqualTo("GUARAGORN_IDLA")
        assertThat((subChunks.children[0].payload as W3dCompressedAnimationHeader).hierarchyName).isEqualTo("GUARAGORN_SKL")
    }
}
