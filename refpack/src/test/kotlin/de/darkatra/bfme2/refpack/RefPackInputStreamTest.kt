package de.darkatra.bfme2.refpack

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class RefPackInputStreamTest {

    @Test
    fun `should read refpack compressed file`() {

        val inputStream = TestUtils.getInputStream(TestUtils.REFPACK_COMPRESSED)

        val actual = RefPackInputStream(inputStream).use { it.readAllBytes() }
        val expected = TestUtils.getInputStream(TestUtils.UNCOMPRESSED).use { it.readAllBytes() }

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should read refpack compressed file byte by byte`() {

        val inputStream = TestUtils.getInputStream(TestUtils.REFPACK_COMPRESSED)

        val actual = ByteArrayOutputStream().use { output ->
            RefPackInputStream(inputStream).use { refPackInputStream ->
                var curByte: Int
                while (refPackInputStream.read().also { curByte = it } != -1) {
                    output.write(curByte)
                }
            }
            output.toByteArray()
        }
        val expected = TestUtils.getInputStream(TestUtils.UNCOMPRESSED).use { it.readAllBytes() }

        assertThat(actual).isEqualTo(expected)
    }
}
