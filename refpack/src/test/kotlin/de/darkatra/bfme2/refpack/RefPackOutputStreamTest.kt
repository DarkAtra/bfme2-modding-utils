package de.darkatra.bfme2.refpack

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class RefPackOutputStreamTest {

    @Test
    fun `should write refpack compressed file`() {

        val inputStream = TestUtils.getInputStream(TestUtils.UNCOMPRESSED)
        val actual = ByteArrayOutputStream().use { output ->
            RefPackOutputStream(output).use { refPackOutputStream ->
                inputStream.copyTo(refPackOutputStream)
            }
            output.toByteArray()
        }

        val expected = TestUtils.getInputStream(TestUtils.REFPACK_COMPRESSED).use { it.readAllBytes() }

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should write refpack compressed file byte by byte`() {

        val inputStream = TestUtils.getInputStream(TestUtils.UNCOMPRESSED)
        val actual = ByteArrayOutputStream().use { output ->
            RefPackOutputStream(output).use { refPackOutputStream ->
                var curByte: Int
                while (inputStream.read().also { curByte = it } != -1) {
                    refPackOutputStream.write(curByte)
                }
            }
            output.toByteArray()
        }

        val expected = TestUtils.getInputStream(TestUtils.REFPACK_COMPRESSED).use { it.readAllBytes() }

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should round-trip refpack compression`() {

        val inputStream = TestUtils.getInputStream(TestUtils.UNCOMPRESSED)
        val compressed = ByteArrayOutputStream().use { output ->
            RefPackOutputStream(output).use { refPackOutputStream ->
                inputStream.copyTo(refPackOutputStream)
            }
            output.toByteArray()
        }

        val actual = RefPackInputStream(compressed.inputStream()).use { it.readAllBytes() }
        val expected = TestUtils.getInputStream(TestUtils.UNCOMPRESSED).use { it.readAllBytes() }

        assertThat(actual).isEqualTo(expected)
    }
}
