package de.darkatra.bfme2.assetdat.model

import com.google.common.io.ByteStreams
import de.darkatra.bfme2.assetdat.AssetDatFileReader
import de.darkatra.bfme2.assetdat.AssetDatFileWriter
import de.darkatra.bfme2.assetdat.TestUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class AssetDatFileTest {

    private val assetDatFileReader = AssetDatFileReader()
    private val assetDatFileWriter = AssetDatFileWriter()

    @Test
    fun `should merge asset-1 and asset-2 into asset-merged`() {

        val asset1 = TestUtils.getInputStream("/assetdats/asset-1.dat").use(assetDatFileReader::read)

        assertThat(asset1.assets).hasSize(3)
        assertThat(asset1.assets.map { it.name }).containsExactlyInAnyOrder("exaragornanduril.tga", "guaragorn_rotk.tga", "guaragorn_skn.w3d")

        val asset2 = TestUtils.getInputStream("/assetdats/asset-2.dat").use(assetDatFileReader::read)

        assertThat(asset2.assets).hasSize(3)
        assertThat(asset2.assets.map { it.name }).containsExactlyInAnyOrder("gaduz.tga", "gaduz.w3d", "gaduzdrum.tga")

        val expectedAssetMerged = TestUtils.getInputStream("/assetdats/asset-1-2-merged.dat").use(assetDatFileReader::read)

        assertThat(expectedAssetMerged.assets).hasSize(6)
        assertThat(expectedAssetMerged.assets.map { it.name }).containsExactlyInAnyOrder(
            "gaduz.tga",
            "gaduz.w3d",
            "gaduzdrum.tga",
            "exaragornanduril.tga",
            "guaragorn_rotk.tga",
            "guaragorn_skn.w3d"
        )

        val assetMerged = asset1.merge(asset2)

        assertThat(assetMerged).isEqualTo(expectedAssetMerged)
    }

    @Test
    fun `should merge asset-2 and asset-3 overwriting existing assets`() {

        val asset2 = TestUtils.getInputStream("/assetdats/asset-2.dat").use(assetDatFileReader::read)

        assertThat(asset2.assets).hasSize(3)
        assertThat(asset2.assets.map { it.name }).containsExactlyInAnyOrder("gaduz.tga", "gaduz.w3d", "gaduzdrum.tga")
        assertThat(asset2.assets.find { it.name == "gaduz.w3d" }!!.dependencies.sumOf { it.size }).isEqualTo(65282u)

        val asset3 = TestUtils.getInputStream("/assetdats/asset-3.dat").use(assetDatFileReader::read)

        assertThat(asset3.assets).hasSize(3)
        assertThat(asset3.assets.map { it.name }).containsExactlyInAnyOrder("gaduz.tga", "gaduz.w3d", "gaduzdrum2.tga")
        assertThat(asset3.assets.find { it.name == "gaduz.w3d" }!!.dependencies.sumOf { it.size }).isEqualTo(65283u)

        val expectedAssetMerged = TestUtils.getInputStream("/assetdats/asset-2-3-merged.dat").use(assetDatFileReader::read)

        assertThat(expectedAssetMerged.assets).hasSize(4)
        assertThat(expectedAssetMerged.assets.map { it.name }).containsExactlyInAnyOrder(
            "gaduz.tga",
            "gaduz.w3d",
            "gaduzdrum.tga",
            "gaduzdrum2.tga",
        )
        assertThat(expectedAssetMerged.assets.find { it.name == "gaduz.w3d" }!!.dependencies.sumOf { it.size }).isEqualTo(65283u)

        val assetMerged = asset2.merge(asset3)

        assertThat(assetMerged).isEqualTo(expectedAssetMerged)
    }

    @Test
    fun `should have the same byte size when writing a merged assetdat`() {

        val expectedFileSize = TestUtils.getInputStream("/assetdats/asset-2-3-merged.dat").use(ByteStreams::exhaust)
        val asset2 = TestUtils.getInputStream("/assetdats/asset-2.dat").use(assetDatFileReader::read)
        val asset3 = TestUtils.getInputStream("/assetdats/asset-3.dat").use(assetDatFileReader::read)

        val bytesWritten = ByteArrayOutputStream().use {
            assetDatFileWriter.write(it, asset2.merge(asset3))
            it.size()
        }

        assertThat(bytesWritten).isEqualTo(expectedFileSize)
    }
}
