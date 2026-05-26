package de.darkatra.bfme2.assetdat

import com.google.common.io.ByteStreams
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class AssetDatFileWriterTest {

    @Test
    fun `should have the same byte size when writing assetdat`() {

        val expectedFileSize = TestUtils.getInputStream("/assetdats/bfme2-asset.dat").use(ByteStreams::exhaust)
        val parsedFile = TestUtils.getInputStream("/assetdats/bfme2-asset.dat").use(AssetDatFileReader()::read)

        val bytesWritten = ByteArrayOutputStream().use {
            AssetDatFileWriter().write(it, parsedFile)
            it.size()
        }

        assertThat(bytesWritten).isEqualTo(expectedFileSize)
    }

    @Test
    fun `should produce identical assetdat file when writing a parsed assetdat`() {

        val parsedFile = TestUtils.getInputStream("/assetdats/bfme2-asset.dat").use(AssetDatFileReader()::read)

        val writtenBytes = ByteArrayOutputStream().use {
            AssetDatFileWriter().write(it, parsedFile)
            it.toByteArray()
        }

        val writtenFile = writtenBytes.inputStream().use(AssetDatFileReader()::read)

        assertThat(writtenFile).isEqualTo(parsedFile)
    }
}
