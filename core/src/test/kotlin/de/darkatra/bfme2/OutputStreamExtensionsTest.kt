package de.darkatra.bfme2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets

internal class OutputStreamExtensionsTest {

    @Test
    internal fun `should write boolean`() {

        val outputStream = ByteArrayOutputStream(1)

        outputStream.writeBoolean(true)

        assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0x01))
    }

    @Test
    internal fun `should write byte`() {

        val outputStream = ByteArrayOutputStream(1)

        outputStream.writeByte(0xF1.toByte())

        assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0xF1.toByte()))
    }

    @Test
    internal fun `should write UByte`() {

        val outputStream = ByteArrayOutputStream(1)

        outputStream.writeUByte(0u)
        outputStream.writeUByte(127u)
        outputStream.writeUByte(128u)
        outputStream.writeUByte(255u)

        assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0, 127.toByte(), (-128).toByte(), -1))
    }

    @Nested
    inner class WriteLittleEndian {

        @Test
        internal fun `should write short`() {

            val outputStream = ByteArrayOutputStream(2)

            outputStream.writeShort(1)

            assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0x01, 0x00))
        }

        @Test
        internal fun `should write UShort`() {

            val outputStream = ByteArrayOutputStream(2)

            outputStream.writeUShort(1u)

            assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0x01, 0x00))
        }

        @Test
        internal fun `should write Int`() {

            val outputStream = ByteArrayOutputStream(4)

            outputStream.writeInt(1)

            assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0x01, 0x00, 0x00, 0x00))
        }

        @Test
        internal fun `should write UInt`() {

            val outputStream = ByteArrayOutputStream(4)

            outputStream.writeUInt(1u)

            assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0x01, 0x00, 0x00, 0x00))
        }

        @Test
        internal fun `should write float`() {

            val outputStream = ByteArrayOutputStream(4)

            outputStream.writeFloat(1f)

            assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0x00, 0x00, 0x80.toByte(), 0x3F))
        }

        @Test
        internal fun `should write UShort prefixed string with US_ASCII encoding`() {

            // byte representation of "LothlorienGrass05" with the length (17) prefixed as UShort
            // 17, 0 is the UShort 17
            // 76, 111, 116, 104, 108, 111, 114, 105, 101, 110, 71, 114, 97, 115, 115, 48, 53 is the String "LothlorienGrass05" encoded in US_ASCII
            val expectedBytes = byteArrayOf(17, 0, 76, 111, 116, 104, 108, 111, 114, 105, 101, 110, 71, 114, 97, 115, 115, 48, 53)

            val outputStream = ByteArrayOutputStream(expectedBytes.size)

            outputStream.writeUShortPrefixedString("LothlorienGrass05")

            assertThat(outputStream.toByteArray()).isEqualTo(expectedBytes)
        }

        @Test
        internal fun `should write UShort prefixed string with UTF_16 encoding`() {

            // byte representation of "Neutral" with the length (7) prefixed as UShort
            // 7, 0 is the UShort 7
            // 78, 0, 101, 0, 117, 0, 116, 0, 114, 0, 97, 0, 108, 0 is the String "Neutral" encoded in UTF_16LE
            val expectedBytes = byteArrayOf(7, 0, 78, 0, 101, 0, 117, 0, 116, 0, 114, 0, 97, 0, 108, 0)

            val outputStream = ByteArrayOutputStream(expectedBytes.size)

            outputStream.writeUShortPrefixedString("Neutral", StandardCharsets.UTF_16LE)

            assertThat(outputStream.toByteArray()).isEqualTo(expectedBytes)
        }

        @Test
        internal fun `should write UShort prefixed string that has the max allowed length`() {

            val outputStream = ByteArrayOutputStream(UShort.MAX_VALUE.toInt())

            val extremelyLongString = (0u.toUShort() until UShort.MAX_VALUE step 1).joinToString("") { "a" }

            outputStream.writeUShortPrefixedString(extremelyLongString)

            // expect a byte array starting with 2 bits representing the max value of a UShort
            // followed by 65535 times 97 which represents the letter 'a' in ASCII
            assertThat(outputStream.toByteArray()).isEqualTo(
                byteArrayOf(
                    -1, -1, *(0u.toUShort() until UShort.MAX_VALUE step 1).map { 97.toByte() }.toByteArray()
                )
            )
        }

        @Test
        internal fun `should not write UShort prefixed string that is too long`() {

            val outputStream = ByteArrayOutputStream(0)

            // one character above the allowed string length
            val extremelyLongString = (0u until UShort.MAX_VALUE.toUInt() + 1u step 1).joinToString("") { "a" }

            assertThrows<IllegalArgumentException> {
                outputStream.writeUShortPrefixedString(extremelyLongString)
            }
        }

        @Test
        internal fun `should write max Int value as 7 bit Int`() {

            val outputStream = ByteArrayOutputStream()

            outputStream.write7BitInt(Int.MAX_VALUE)

            assertThat(outputStream.toByteArray()).isEqualTo(
                // 7 bit encoded, little endian version of Int.MAX_VALUE (0x7FFFFFFF)
                byteArrayOf(
                    0xFF.toByte(), // set most significant bit means: continue reading next bit
                    0xFF.toByte(), // so only the 7 least significant bits are actual integer data
                    0xFF.toByte(), // e.g. there are 28 set bits in the first 4 bytes
                    0xFF.toByte(), // resulting in 0x0FFFFFFF
                    0b00000111.toByte() // the remaining 4 bits (0b0111) are contained in the fifth bit
                )
            )
        }

        @Test
        internal fun `should write 128 as 7 bit Int`() {

            val outputStream = ByteArrayOutputStream()

            outputStream.write7BitInt(128)

            assertThat(outputStream.toByteArray()).isEqualTo(
                // 7 bit encoded, little endian version of 128 (0x80)
                byteArrayOf(
                    0b1000_0000.toByte(), // set most significant bit means: continue reading next bit
                    0x01.toByte(), // only the least significant bit is set resulting in 0b1000_0000 (0x80)
                )
            )
        }

        @Test
        internal fun `should write zero as 7 bit Int`() {

            val outputStream = ByteArrayOutputStream()

            outputStream.write7BitInt(0)

            assertThat(outputStream.toByteArray()).isEqualTo(
                // 7 bit encoded, little endian version of 0 (0x00)
                byteArrayOf(0x00.toByte())
            )
        }

        @Test
        internal fun `should write negative one as 7 bit Int`() {

            val outputStream = ByteArrayOutputStream()

            outputStream.write7BitInt(-1)

            assertThat(outputStream.toByteArray()).isEqualTo(
                // 7 bit encoded, little endian version of -1 (0xFFFFFFFF)
                byteArrayOf(
                    0xFF.toByte(), // set most significant bit means: continue reading next bit
                    0xFF.toByte(), // so only the 7 least significant bits are actual integer data
                    0xFF.toByte(), // e.g. there are 28 set bits in the first 4 bytes
                    0xFF.toByte(), // resulting in 0xFFFFFFFF
                    0x0F.toByte() // the remaining 4 bits (0b1111) are contained in the fifth bit
                )
            )
        }

        @Test
        internal fun `should write min Int value as 7 bit Int`() {

            val outputStream = ByteArrayOutputStream()

            outputStream.write7BitInt(Int.MIN_VALUE)

            assertThat(outputStream.toByteArray()).isEqualTo(
                // 7 bit encoded, little endian version of Int.MIN_VALUE (0x80000000)
                byteArrayOf(
                    0x80.toByte(), // set most significant bit means: continue reading next bit
                    0x80.toByte(), // so only the 7 least significant bits are actual integer data
                    0x80.toByte(), // e.g. there are 28 unset bits in the first 4 bytes
                    0x80.toByte(), // resulting in 0x00000000
                    0x08.toByte() // the remaining 4 bits (0b1000) are contained in the fifth bit
                )
            )
        }

        @Test
        internal fun `should fail to read 7 bit Int with invalid fifth byte`() {

            val outputStream = ByteArrayOutputStream()

            outputStream.writeBytes(
                byteArrayOf(
                    0xFF.toByte(),
                    0xFF.toByte(),
                    0xFF.toByte(),
                    0xFF.toByte(),
                    0x1F.toByte() // fifth bit has 5 set bits which is invalid for 7 bit encoded ints
                )
            )

            assertThrows<NumberFormatException> {
                outputStream.toByteArray().inputStream().use { it.read7BitInt() }
            }
        }

        @Test
        internal fun `should write 7 bit Int prefixed string`() {

            val outputStream = ByteArrayOutputStream()

            val testString = (0 until 100).joinToString("") { "a" }

            outputStream.write7BitIntPrefixedString(testString)

            assertThat(outputStream.toByteArray()).isEqualTo(
                byteArrayOf(
                    100, *(0 until 100).map { 97.toByte() }.toByteArray()
                )
            )
        }

        @Test
        internal fun `should write 7 bit Int prefixed string with length of 128 characters`() {

            val outputStream = ByteArrayOutputStream()

            val testString = (0 until 128).joinToString("") { "a" }

            outputStream.write7BitIntPrefixedString(testString)

            assertThat(outputStream.toByteArray()).isEqualTo(
                byteArrayOf(
                    128.toUByte().toByte(), 1, *(0 until 128).map { 97.toByte() }.toByteArray()
                )
            )
        }

        @Test
        internal fun `should roundtrip 7 bit Int prefixed string`() {

            val outputStream = ByteArrayOutputStream()

            val testString = (0 until 128).joinToString("") { "a" }

            outputStream.write7BitIntPrefixedString(testString)

            assertThat(outputStream.toByteArray().inputStream().use { it.read7BitIntPrefixedString() }).isEqualTo(testString)
        }

        @Test
        internal fun `should roundtrip 7 bit Int prefixed string with two byte character`() {

            val outputStream = ByteArrayOutputStream()

            val testString = "ü"

            outputStream.write7BitIntPrefixedString(testString)

            assertThat(outputStream.toByteArray().inputStream().use { it.read7BitIntPrefixedString() }).isEqualTo(testString)
        }

        @Test
        internal fun `should roundtrip 7 bit Int prefixed string with zero length`() {

            val outputStream = ByteArrayOutputStream()

            val testString = ""

            outputStream.write7BitIntPrefixedString(testString)

            assertThat(outputStream.toByteArray().inputStream().use { it.read7BitIntPrefixedString() }).isEqualTo(testString)
        }

        @Test
        internal fun `should fail to read 7 bit Int prefixed string with negative length`() {

            val outputStream = ByteArrayOutputStream()
            outputStream.write7BitInt(-1)

            assertThrows<IllegalStateException> {
                outputStream.toByteArray().inputStream().use { it.read7BitIntPrefixedString() }
            }
        }
    }
}
