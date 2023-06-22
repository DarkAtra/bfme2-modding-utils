package de.darkatra.bfme2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

internal class InputStreamExtensionsTest {

    @Test
    internal fun shouldReadBoolean() {

        val bytes = byteArrayOf(0x01)

        val inputStream = ByteArrayInputStream(bytes)

        assertThat(inputStream.readBoolean()).isEqualTo(true)
    }

    @Test
    internal fun shouldReadByte() {

        val bytes = byteArrayOf(
            0xF1.toByte()
        )

        val inputStream = ByteArrayInputStream(bytes)

        assertThat(inputStream.readByte()).isEqualTo(0xF1.toByte())
    }

    @Nested
    inner class ReadLittleEndian {

        @Test
        internal fun shouldReadShort() {

            val bytes = byteArrayOf(0x01, 0x00)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.readShort()).isEqualTo(1)
        }

        @Test
        internal fun shouldReadUShort() {

            val bytes = byteArrayOf(0x01, 0x00)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.readUShort()).isEqualTo(1u.toUShort())
        }

        @Test
        internal fun shouldReadInt() {

            val bytes = byteArrayOf(0x01, 0x00, 0x00, 0x00)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.readInt()).isEqualTo(1)
        }

        @Test
        internal fun shouldReadUInt() {

            val bytes = byteArrayOf(0x01, 0x00, 0x00, 0x00)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.readUInt()).isEqualTo(1u)
        }

        @Test
        internal fun shouldReadULong() {

            val bytes = byteArrayOf(0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.readULong()).isEqualTo(1uL)
        }

        @Test
        internal fun shouldReadFloat() {

            val bytes = byteArrayOf(0x00, 0x00, 0x80.toByte(), 0x3F)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.readFloat()).isEqualTo(1f)
        }

        @Test
        internal fun shouldReadNullTerminatedString() {

            val bytes = "Hello".toByteArray() + 0.toByte()

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.readNullTerminatedString()).isEqualTo("Hello")
        }

        @Test
        internal fun shouldFailToReadStringWithoutNullTerminator() {

            val bytes = "Hello".toByteArray()

            val inputStream = ByteArrayInputStream(bytes)

            assertThrows<InvalidDataException> {
                inputStream.readNullTerminatedString()
            }
        }

        @Test
        internal fun shouldReadUShortPrefixedStringWithUS_ASCIIEncoding() {

            // byte representation of "LothlorienGrass05" in US_ASCII with the length (17) prefixed as UShort
            // 17, 0 is the UShort 17
            // 76, 111, 116, 104, 108, 111, 114, 105, 101, 110, 71, 114, 97, 115, 115, 48, 53 is the String "LothlorienGrass05" encoded in US_ASCII
            val bytes = byteArrayOf(17, 0, 76, 111, 116, 104, 108, 111, 114, 105, 101, 110, 71, 114, 97, 115, 115, 48, 53)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.readUShortPrefixedString()).isEqualTo("LothlorienGrass05")
        }

        @Test
        internal fun shouldReadUShortPrefixedStringWithUTF_16Encoding() {

            // byte representation of "Neutral" in UTF_16LE with the length (7) prefixed as UShort
            // 7, 0 is the UShort 7
            // 78, 0, 101, 0, 117, 0, 116, 0, 114, 0, 97, 0, 108, 0 is the String "Neutral" encoded in UTF_16LE
            val bytes = byteArrayOf(7, 0, 78, 0, 101, 0, 117, 0, 116, 0, 114, 0, 97, 0, 108, 0)

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.readUShortPrefixedString(StandardCharsets.UTF_16LE)).isEqualTo("Neutral")
        }

        @Test
        internal fun shouldReadMaxIntValueFrom7BitInt() {

            // 7 bit encoded, little endian version of Int.MAX_VALUE (0x7FFFFFFF)
            val bytes = byteArrayOf(
                0xFF.toByte(), // set most significant bit means: continue reading next bit
                0xFF.toByte(), // so only the 7 least significant bits are actual integer data
                0xFF.toByte(), // e.g. there are 28 set bits in the first 4 bytes
                0xFF.toByte(), // resulting in 0x0FFFFFFF
                0b00000111.toByte() // the remaining 4 bits (0b0111) are contained in the fifth bit
            )

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.read7BitInt()).isEqualTo(Int.MAX_VALUE)
        }

        @Test
        internal fun shouldRead128From7BitInt() {

            // 7 bit encoded, little endian version of 128 (0x80)
            val bytes = byteArrayOf(
                0b1000_0000.toByte(), // set most significant bit means: continue reading next bit
                0x01.toByte(), // only the least significant bit is set resulting in 0b1000_0000 (0x80)
            )

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.read7BitInt()).isEqualTo(128)
        }

        @Test
        internal fun shouldReadZeroFrom7BitInt() {

            // 7 bit encoded, little endian version of 0 (0x00)
            val bytes = byteArrayOf(0x00.toByte())

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.read7BitInt()).isEqualTo(0)
        }

        @Test
        internal fun shouldReadNegativeOneFrom7BitInt() {

            // 7 bit encoded, little endian version of -1 (0xFFFFFFFF)
            val bytes = byteArrayOf(
                0xFF.toByte(), // set most significant bit means: continue reading next bit
                0xFF.toByte(), // so only the 7 least significant bits are actual integer data
                0xFF.toByte(), // e.g. there are 28 set bits in the first 4 bytes
                0xFF.toByte(), // resulting in 0xFFFFFFFF
                0x0F.toByte() // the remaining 4 bits (0b1111) are contained in the fifth bit
            )

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.read7BitInt()).isEqualTo(-1)
        }

        @Test
        internal fun shouldReadMinIntValueFrom7BitInt() {

            // 7 bit encoded, little endian version of Int.MIN_VALUE (0x80000000)
            val bytes = byteArrayOf(
                0x80.toByte(), // set most significant bit means: continue reading next bit
                0x80.toByte(), // so only the 7 least significant bits are actual integer data
                0x80.toByte(), // e.g. there are 28 unset bits in the first 4 bytes
                0x80.toByte(), // resulting in 0x00000000
                0x08.toByte() // the remaining 4 bits (0b1000) are contained in the fifth bit
            )

            val inputStream = ByteArrayInputStream(bytes)

            assertThat(inputStream.read7BitInt()).isEqualTo(Int.MIN_VALUE)
        }
    }
}
