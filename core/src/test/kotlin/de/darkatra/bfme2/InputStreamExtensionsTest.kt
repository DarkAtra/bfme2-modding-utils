package de.darkatra.bfme2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.EOFException
import java.io.InputStream
import java.nio.charset.StandardCharsets

internal class InputStreamExtensionsTest {

    @Test
    internal fun `should readNBytes with UInt length`() {

        val infiniteInputStream = object : InputStream() {
            override fun read(): Int {
                return 1
            }
        }

        val bytesRead = infiniteInputStream.use { it.readNBytes(1u) }.size

        assertThat(bytesRead).isEqualTo(1)
    }

    @Test
    internal fun `should not readNBytes with UInt length that exceeds Int MAX_VALUE`() {

        val infiniteInputStream = object : InputStream() {
            override fun read(): Int {
                return 1
            }
        }

        assertThrows<IllegalStateException> {
            infiniteInputStream.use { it.readNBytes(Int.MAX_VALUE.toUInt() + 1u) }
        }
    }

    @Test
    internal fun `should read boolean`() {

        val bytes = byteArrayOf(0x01)

        val inputStream = ByteArrayInputStream(bytes)

        assertThat(inputStream.use { it.readBoolean() }).isEqualTo(true)
    }

    @Test
    internal fun `should read byte`() {

        val bytes = byteArrayOf(
            0xF1.toByte()
        )

        val inputStream = ByteArrayInputStream(bytes)

        assertThat(inputStream.use { it.readByte() }).isEqualTo(0xF1.toByte())
    }

    @Test
    internal fun `should not read byte`() {

        val bytes = byteArrayOf()

        val inputStream = ByteArrayInputStream(bytes)

        assertThrows<EOFException> {
            inputStream.use { it.readByte() }
        }
    }

    @Test
    internal fun `should read UByte`() {

        val bytes = byteArrayOf(
            0xF1.toByte()
        )

        val inputStream = ByteArrayInputStream(bytes)

        assertThat(inputStream.use { it.readUByte() }).isEqualTo(0xF1.toUByte())
    }

    @Test
    internal fun `should read null terminated string`() {

        val bytes = "Hello".toByteArray() + 0.toByte()

        val inputStream = ByteArrayInputStream(bytes)

        assertThat(inputStream.use { it.readNullTerminatedString() }).isEqualTo("Hello")
    }

    @Test
    internal fun `should fail to read string without null terminator`() {

        val bytes = "Hello".toByteArray()

        val inputStream = ByteArrayInputStream(bytes)

        assertThrows<InvalidDataException> {
            inputStream.use { it.readNullTerminatedString() }
        }
    }

    @Test
    internal fun `should read null terminated string with fixed length`() {

        val bytes = "Hello".toByteArray() + 0.toByte() + 0.toByte() + 0.toByte() + 0.toByte()

        val inputStream = ByteArrayInputStream(bytes)

        inputStream.use {
            assertThat(it.readNullTerminatedString(fixedLength = 9u)).isEqualTo("Hello")
            assertThat(inputStream.available()).isZero()
        }
    }

    @Test
    internal fun `should read null terminated string with fixed length and leave bytes beyond the limit untouched`() {

        val bytes = "Hello".toByteArray() + 0.toByte() + 0.toByte() + 0.toByte() + 0.toByte()

        val inputStream = ByteArrayInputStream(bytes)

        inputStream.use {
            assertThat(it.readNullTerminatedString(fixedLength = 7u)).isEqualTo("Hello")
            assertThat(inputStream.available()).isEqualTo(2)
        }
    }

    @Test
    internal fun `should failed to read null terminated string with fixed length if some bytes could not be read`() {

        val bytes = "Hello".toByteArray() + 0.toByte() + 0.toByte() + 0.toByte() + 0.toByte()

        val inputStream = ByteArrayInputStream(bytes)

        assertThrows<InvalidDataException> {
            inputStream.use { it.readNullTerminatedString(fixedLength = 10u) }
        }
    }

    @Nested
    inner class ReadLittleEndian {

        @Test
        internal fun `should read short`() {

            val bytes = byteArrayOf(0x01, 0x00)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.use { it.readShort() }).isEqualTo(1)
        }

        @Test
        internal fun `should read UShort`() {

            val bytes = byteArrayOf(0x01, 0x00)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.use { it.readUShort() }).isEqualTo(1u.toUShort())
        }

        @Test
        internal fun `should read Int`() {

            val bytes = byteArrayOf(0x01, 0x00, 0x00, 0x00)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.use { it.readInt() }).isEqualTo(1)
        }

        @Test
        internal fun `should read UInt`() {

            val bytes = byteArrayOf(0x01, 0x00, 0x00, 0x00)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.use { it.readUInt() }).isEqualTo(1u)
        }

        @Test
        internal fun `should read ULong`() {

            val bytes = byteArrayOf(0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.use { it.readULong() }).isEqualTo(1uL)
        }

        @Test
        internal fun `should read float`() {

            val bytes = byteArrayOf(0x00, 0x00, 0x80.toByte(), 0x3F)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.use { it.readFloat() }).isEqualTo(1f)
        }

        @Test
        internal fun `should read UShort prefixed string with US_ASCII encoding`() {

            // byte representation of "LothlorienGrass05" in US_ASCII with the length (17) prefixed as UShort
            // 17, 0 is the UShort 17
            // 76, 111, 116, 104, 108, 111, 114, 105, 101, 110, 71, 114, 97, 115, 115, 48, 53 is the String "LothlorienGrass05" encoded in US_ASCII
            val bytes = byteArrayOf(17, 0, 76, 111, 116, 104, 108, 111, 114, 105, 101, 110, 71, 114, 97, 115, 115, 48, 53)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.use { it.readUShortPrefixedString() }).isEqualTo("LothlorienGrass05")
        }

        @Test
        internal fun `should read UShort prefixed string with UTF_16 encoding`() {

            // byte representation of "Neutral" in UTF_16LE with the length (7) prefixed as UShort
            // 7, 0 is the UShort 7
            // 78, 0, 101, 0, 117, 0, 116, 0, 114, 0, 97, 0, 108, 0 is the String "Neutral" encoded in UTF_16LE
            val bytes = byteArrayOf(7, 0, 78, 0, 101, 0, 117, 0, 116, 0, 114, 0, 97, 0, 108, 0)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.use { it.readUShortPrefixedString(StandardCharsets.UTF_16LE) }).isEqualTo("Neutral")
        }

        @Test
        internal fun `should read max Int value from 7 bit Int`() {

            // 7 bit encoded, little endian version of Int.MAX_VALUE (0x7FFFFFFF)
            val bytes = byteArrayOf(
                0xFF.toByte(), // set most significant bit means: continue reading next bit
                0xFF.toByte(), // so only the 7 least significant bits are actual integer data
                0xFF.toByte(), // e.g. there are 28 set bits in the first 4 bytes
                0xFF.toByte(), // resulting in 0x0FFFFFFF
                0b00000111.toByte() // the remaining 4 bits (0b0111) are contained in the fifth bit
            )

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.use { it.read7BitInt() }).isEqualTo(Int.MAX_VALUE)
        }

        @Test
        internal fun `should read 128 from 7 bit Int`() {

            // 7 bit encoded, little endian version of 128 (0x80)
            val bytes = byteArrayOf(
                0b1000_0000.toByte(), // set most significant bit means: continue reading next bit
                0x01.toByte(), // only the least significant bit is set resulting in 0b1000_0000 (0x80)
            )

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.use { it.read7BitInt() }).isEqualTo(128)
        }

        @Test
        internal fun `should read zero from 7 bit Int`() {

            // 7 bit encoded, little endian version of 0 (0x00)
            val bytes = byteArrayOf(0x00.toByte())

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.use { it.read7BitInt() }).isEqualTo(0)
        }

        @Test
        internal fun `should read negative one from 7 bit Int`() {

            // 7 bit encoded, little endian version of -1 (0xFFFFFFFF)
            val bytes = byteArrayOf(
                0xFF.toByte(), // set most significant bit means: continue reading next bit
                0xFF.toByte(), // so only the 7 least significant bits are actual integer data
                0xFF.toByte(), // e.g. there are 28 set bits in the first 4 bytes
                0xFF.toByte(), // resulting in 0xFFFFFFFF
                0x0F.toByte() // the remaining 4 bits (0b1111) are contained in the fifth bit
            )

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.use { it.read7BitInt() }).isEqualTo(-1)
        }

        @Test
        internal fun `should read min Int value from 7 bit Int`() {

            // 7 bit encoded, little endian version of Int.MIN_VALUE (0x80000000)
            val bytes = byteArrayOf(
                0x80.toByte(), // set most significant bit means: continue reading next bit
                0x80.toByte(), // so only the 7 least significant bits are actual integer data
                0x80.toByte(), // e.g. there are 28 unset bits in the first 4 bytes
                0x80.toByte(), // resulting in 0x00000000
                0x08.toByte() // the remaining 4 bits (0b1000) are contained in the fifth bit
            )

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.use { it.read7BitInt() }).isEqualTo(Int.MIN_VALUE)
        }

        @Test
        internal fun `should fail to read 7 bit Int with invalid fifth byte`() {

            val outputStream = ByteArrayOutputStream()
            outputStream.use {
                it.writeBytes(
                    byteArrayOf(
                        0xFF.toByte(),
                        0xFF.toByte(),
                        0xFF.toByte(),
                        0xFF.toByte(),
                        0x1F.toByte() // fifth bit has 5 set bits which is invalid for 7 bit encoded ints
                    )
                )
            }

            assertThrows<NumberFormatException> {
                outputStream.toByteArray().inputStream().use { it.read7BitInt() }
            }
        }
    }
}
