package de.darkatra.bfme2.assetdat

import de.darkatra.bfme2.assetdat.model.DependencyKind
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AssetDatFileReaderTest {

    private val assetDatFileReader = AssetDatFileReader()

    @Test
    fun `should read w3d file`() {

        val assetDatFile = TestUtils.getInputStream("/assetdats/bfme2-asset.dat").use(assetDatFileReader::read)

        assertThat(assetDatFile.assets).hasSize(17657)
        assertThat(assetDatFile.assets.flatMap { it.dependencies }.filter { it.extraDependencyNames.isNotEmpty() }).hasSize(26981)

        val aragorn = assetDatFile.assets.find { it.name == "guaragorn_skn.w3d" }!!
        assertThat(aragorn.fileTime).isEqualTo("2005-11-28T19:56:32.485320200Z")
        assertThat(aragorn.dependencies).hasSize(4)

        assertThat(aragorn.dependencies[0].name).isEqualTo("GUARAGORN_SKN.ARAGORN MESH")
        assertThat(aragorn.dependencies[0].kind).isEqualTo(DependencyKind.MESH)
        assertThat(aragorn.dependencies[0].offset).isEqualTo(0u)
        assertThat(aragorn.dependencies[0].size).isEqualTo(47356u)
        assertThat(aragorn.dependencies[0].extraDependencyNames).containsExactly("guaragorn_rotk.tga")

        assertThat(aragorn.dependencies[1].name).isEqualTo("GUARAGORN_SKN.PLANE02")
        assertThat(aragorn.dependencies[1].kind).isEqualTo(DependencyKind.MESH)
        assertThat(aragorn.dependencies[1].offset).isEqualTo(47356u)
        assertThat(aragorn.dependencies[1].size).isEqualTo(1938u)
        assertThat(aragorn.dependencies[1].extraDependencyNames).containsExactly("exaragornanduril.tga")

        assertThat(aragorn.dependencies[2].name).isEqualTo("GUARAGORN_SKN.BOUNDINGBOX")
        assertThat(aragorn.dependencies[2].kind).isEqualTo(DependencyKind.BOX)
        assertThat(aragorn.dependencies[2].offset).isEqualTo(49294u)
        assertThat(aragorn.dependencies[2].size).isEqualTo(76u)
        assertThat(aragorn.dependencies[2].extraDependencyNames).isEmpty()

        assertThat(aragorn.dependencies[3].name).isEqualTo("GUARAGORN_SKN")
        assertThat(aragorn.dependencies[3].kind).isEqualTo(DependencyKind.HLOD)
        assertThat(aragorn.dependencies[3].offset).isEqualTo(49370u)
        assertThat(aragorn.dependencies[3].size).isEqualTo(212u)
        assertThat(aragorn.dependencies[3].extraDependencyNames).containsExactly(
            "guaragorn_skn.aragorn mesh",
            "guaragorn_skn.boundingbox",
            "guaragorn_skn.plane02",
            "h*guaragorn_skl",
        )
    }
}
