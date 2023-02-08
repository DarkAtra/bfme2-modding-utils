package de.darkatra.bfme2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets

internal class OutputStreamExtensionsTest {

    @Test
    internal fun shouldWriteBoolean() {

        val outputStream = ByteArrayOutputStream(1)

        outputStream.writeBoolean(true)

        assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0x01))
    }

    @Test
    internal fun shouldWriteByte() {

        val outputStream = ByteArrayOutputStream(1)

        outputStream.writeByte(0xF1.toByte())

        assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0xF1.toByte()))
    }

    @Nested
    inner class WriteLittleEndian {

        @Test
        internal fun shouldWriteShort() {

            val outputStream = ByteArrayOutputStream(2)

            outputStream.writeShort(1)

            assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0x01, 0x00))
        }

        @Test
        internal fun shouldWriteUShort() {

            val outputStream = ByteArrayOutputStream(2)

            outputStream.writeUShort(1u)

            assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0x01, 0x00))
        }

        @Test
        internal fun shouldWriteInt() {

            val outputStream = ByteArrayOutputStream(4)

            outputStream.writeInt(1)

            assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0x01, 0x00, 0x00, 0x00))
        }

        @Test
        internal fun shouldWriteUInt() {

            val outputStream = ByteArrayOutputStream(4)

            outputStream.writeUInt(1u)

            assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0x01, 0x00, 0x00, 0x00))
        }

        @Test
        internal fun shouldWriteFloat() {

            val outputStream = ByteArrayOutputStream(4)

            outputStream.writeFloat(1f)

            assertThat(outputStream.toByteArray()).isEqualTo(byteArrayOf(0x00, 0x00, 0x80.toByte(), 0x3F))
        }

        @Test
        internal fun shouldWriteUShortPrefixedStringWithUS_ASCIIEncoding() {

            // byte representation of "LothlorienGrass05" with the length (17) prefixed as UShort
            // 17, 0 is the UShort 17
            // 76, 111, 116, 104, 108, 111, 114, 105, 101, 110, 71, 114, 97, 115, 115, 48, 53 is the String "LothlorienGrass05" encoded in US_ASCII
            val expectedBytes = byteArrayOf(17, 0, 76, 111, 116, 104, 108, 111, 114, 105, 101, 110, 71, 114, 97, 115, 115, 48, 53)

            val outputStream = ByteArrayOutputStream(expectedBytes.size)

            outputStream.writeUShortPrefixedString("LothlorienGrass05")

            assertThat(outputStream.toByteArray()).isEqualTo(expectedBytes)
        }

        @Test
        internal fun shouldWriteUShortPrefixedStringWithUTF_16Encoding() {

            // byte representation of "Neutral" with the length (7) prefixed as UShort
            // 7, 0 is the UShort 7
            // 78, 0, 101, 0, 117, 0, 116, 0, 114, 0, 97, 0, 108, 0 is the String "Neutral" encoded in UTF_16LE
            val expectedBytes = byteArrayOf(7, 0, 78, 0, 101, 0, 117, 0, 116, 0, 114, 0, 97, 0, 108, 0)

            val outputStream = ByteArrayOutputStream(expectedBytes.size)

            outputStream.writeUShortPrefixedString("Neutral", StandardCharsets.UTF_16LE)

            assertThat(outputStream.toByteArray()).isEqualTo(expectedBytes)
        }

        @Test
        internal fun shouldWriteUShortPrefixedStringThatHasTheMaxAllowedLength() {

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
        internal fun shouldNotWriteUShortPrefixedStringThatIsTooLong() {

            val outputStream = ByteArrayOutputStream(0)

            // one character above the allowed string length
            val extremelyLongString = (0u until UShort.MAX_VALUE.toUInt() + 1u step 1).joinToString("") { "a" }

            assertThrows<IllegalArgumentException> {
                outputStream.writeUShortPrefixedString(extremelyLongString)
            }
        }

        @Test
        internal fun shouldWrite7BitString() {

            val outputStream = ByteArrayOutputStream()

            val testString = (0 until 100).joinToString("") { "a" }

            outputStream.write7BitString(testString)

            assertThat(outputStream.toByteArray()).isEqualTo(
                byteArrayOf(
                    100, *(0 until 100).map { 97.toByte() }.toByteArray()
                )
            )
        }

        @Test
        internal fun shouldWrite7BitStringWithLengthOf200Characters() {

            val outputStream = ByteArrayOutputStream()

            val testString = (0 until 200).joinToString("") { "a" }

            outputStream.write7BitString(testString)

            assertThat(outputStream.toByteArray()).isEqualTo(
                byteArrayOf(
                    200.toUByte().toByte(), 1, *(0 until 200).map { 97.toByte() }.toByteArray()
                )
            )
        }

        @Test
        internal fun shouldRoundtrip7BitString() {

            val outputStream = ByteArrayOutputStream()

            val testString = (0 until 200).joinToString("") { "a" }

            outputStream.write7BitString(testString)

            assertThat(outputStream.toByteArray().inputStream().read7BitString()).isEqualTo(testString)
        }
    }
}
