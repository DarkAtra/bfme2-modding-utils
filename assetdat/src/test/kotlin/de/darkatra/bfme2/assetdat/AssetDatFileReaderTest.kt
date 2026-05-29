package de.darkatra.bfme2.assetdat

import de.darkatra.bfme2.assetdat.model.AssetEntryKind
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AssetDatFileReaderTest {

    private val assetDatFileReader = AssetDatFileReader()

    @Test
    fun `should read w3d file`() {

        val assetDatFile = TestUtils.getInputStream("/assetdats/bfme2-asset.dat").use(assetDatFileReader::read)

        assertThat(assetDatFile.assets).hasSize(17657)
        assertThat(assetDatFile.assets.flatMap { it.assetEntries }.filter { it.dependencyNames.isNotEmpty() }).hasSize(26981)

        val aragorn = assetDatFile.assets.find { it.name == "guaragorn_skn.w3d" }!!
        assertThat(aragorn.fileTime).isEqualTo("2005-11-28T19:56:32.485320200Z")
        assertThat(aragorn.assetEntries).hasSize(4)

        assertThat(aragorn.assetEntries[0].name).isEqualTo("GUARAGORN_SKN.ARAGORN MESH")
        assertThat(aragorn.assetEntries[0].kind).isEqualTo(AssetEntryKind.MESH)
        assertThat(aragorn.assetEntries[0].offset).isEqualTo(0u)
        assertThat(aragorn.assetEntries[0].size).isEqualTo(47356u)
        assertThat(aragorn.assetEntries[0].dependencyNames).containsExactly("guaragorn_rotk.tga")

        assertThat(aragorn.assetEntries[1].name).isEqualTo("GUARAGORN_SKN.PLANE02")
        assertThat(aragorn.assetEntries[1].kind).isEqualTo(AssetEntryKind.MESH)
        assertThat(aragorn.assetEntries[1].offset).isEqualTo(47356u)
        assertThat(aragorn.assetEntries[1].size).isEqualTo(1938u)
        assertThat(aragorn.assetEntries[1].dependencyNames).containsExactly("exaragornanduril.tga")

        assertThat(aragorn.assetEntries[2].name).isEqualTo("GUARAGORN_SKN.BOUNDINGBOX")
        assertThat(aragorn.assetEntries[2].kind).isEqualTo(AssetEntryKind.BOX)
        assertThat(aragorn.assetEntries[2].offset).isEqualTo(49294u)
        assertThat(aragorn.assetEntries[2].size).isEqualTo(76u)
        assertThat(aragorn.assetEntries[2].dependencyNames).isEmpty()

        assertThat(aragorn.assetEntries[3].name).isEqualTo("GUARAGORN_SKN")
        assertThat(aragorn.assetEntries[3].kind).isEqualTo(AssetEntryKind.HLOD)
        assertThat(aragorn.assetEntries[3].offset).isEqualTo(49370u)
        assertThat(aragorn.assetEntries[3].size).isEqualTo(212u)
        assertThat(aragorn.assetEntries[3].dependencyNames).containsExactly(
            "guaragorn_skn.aragorn mesh",
            "guaragorn_skn.boundingbox",
            "guaragorn_skn.plane02",
            "h*guaragorn_skl",
        )
    }
}
