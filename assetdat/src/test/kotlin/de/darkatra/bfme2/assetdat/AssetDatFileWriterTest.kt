package de.darkatra.bfme2.assetdat

import com.google.common.io.ByteStreams
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.ByteArrayOutputStream
import java.util.stream.Stream

class AssetDatFileWriterTest {

    private val assetDatFileReader = AssetDatFileReader()
    private val assetDatFileWriter = AssetDatFileWriter()

    @ParameterizedTest
    @MethodSource("assetDatsToRoundtrip")
    fun `should have the same byte size when writing assetdat`(assetDatName: String) {

        val expectedFileSize = TestUtils.getInputStream(assetDatName).use(ByteStreams::exhaust)
        val parsedFile = TestUtils.getInputStream(assetDatName).use(assetDatFileReader::read)

        val bytesWritten = ByteArrayOutputStream().use {
            assetDatFileWriter.write(it, parsedFile)
            it.size()
        }

        assertThat(bytesWritten).isEqualTo(expectedFileSize)
    }

    @ParameterizedTest
    @MethodSource("assetDatsToRoundtrip")
    fun `should produce identical assetdat file when writing a parsed assetdat`(assetDatName: String) {

        val parsedFile = TestUtils.getInputStream(assetDatName).use(assetDatFileReader::read)

        val writtenBytes = ByteArrayOutputStream().use {
            assetDatFileWriter.write(it, parsedFile)
            it.toByteArray()
        }

        val writtenFile = writtenBytes.inputStream().use(assetDatFileReader::read)

        assertThat(writtenFile).isEqualTo(parsedFile)
    }

    companion object {
        @JvmStatic
        fun assetDatsToRoundtrip(): Stream<String> {
            return Stream.of(
                "/assetdats/asset-1.dat",
                "/assetdats/asset-2.dat",
                "/assetdats/asset-3.dat",
                "/assetdats/asset-1-2-merged.dat",
                "/assetdats/asset-2-3-merged.dat",
                "/assetdats/bfme2-asset.dat",
            )
        }
    }
}
