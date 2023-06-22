package de.darkatra.bfme2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ConversionExtensionsTest {

    @Test
    internal fun shouldConvertBooleanToByte() {

        assertThat(false.toByte()).isEqualTo(0x00.toByte())
        assertThat(true.toByte()).isEqualTo(0x01.toByte())
    }

    @Test
    internal fun shouldConvertByteToBoolean() {

        assertThat(0x00.toByte().toBoolean()).isEqualTo(false)
        assertThat(0x01.toByte().toBoolean()).isEqualTo(true)
    }

    @Test
    internal fun shouldConvertByteToHexString() {

        val bytes = byteArrayOf(0xCC.toByte(), 0xFF.toByte())
        assertThat(bytes.toHexString()).isEqualTo("cc ff")
    }

    @Test
    internal fun shouldNotConvertByteToBoolean() {

        byteArrayOf(
            0x02,
            0x03,
            0xFF.toByte()
        ).forEach { byte ->
            assertThrows<ConversionException> { byte.toBoolean() }
        }
    }

    @Nested
    inner class ByteArrayToNumbers {

        @Test
        internal fun shouldConvertBigEndianByteArrayToUShort() {

            val expected = listOf(
                0u,
                1u,
                Short.MAX_VALUE.toUShort(),
                UShort.MAX_VALUE
            )

            listOf(
                byteArrayOf(0x00, 0x00),
                byteArrayOf(0x00, 0x01),
                byteArrayOf(0x7F.toByte(), 0xFF.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte()),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianUShort())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertLittleEndianByteArrayToUShort() {

            val expected = listOf(
                0u,
                1u,
                Short.MAX_VALUE.toUShort(),
                UShort.MAX_VALUE
            )

            listOf(
                byteArrayOf(0x00, 0x00),
                byteArrayOf(0x01, 0x00),
                byteArrayOf(0xFF.toByte(), 0x7F.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte()),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianUShort())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertBigEndianByteArrayToShort() {

            val expected = listOf(
                0,
                1,
                Short.MAX_VALUE,
                -1
            )

            listOf(
                byteArrayOf(0x00, 0x00),
                byteArrayOf(0x00, 0x01),
                byteArrayOf(0x7F.toByte(), 0xFF.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte()),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianShort())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertLittleEndianByteArrayToShort() {

            val expected = listOf(
                0,
                1,
                Short.MAX_VALUE,
                -1
            )

            listOf(
                byteArrayOf(0x00, 0x00),
                byteArrayOf(0x01, 0x00),
                byteArrayOf(0xFF.toByte(), 0x7F.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte()),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianShort())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertBigEndianByteArrayToUInt() {

            val expected = listOf(
                0u,
                1u,
                Int.MAX_VALUE.toUInt(),
                UInt.MAX_VALUE
            )

            listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x00, 0x00, 0x00, 0x01),
                byteArrayOf(0x7F.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianUInt())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertLittleEndianByteArrayToUInt() {

            val expected = listOf(
                0u,
                1u,
                Int.MAX_VALUE.toUInt(),
                UInt.MAX_VALUE
            )

            listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x01, 0x00, 0x00, 0x00),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianUInt())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertBigEndianByteArrayToInt() {

            val expected = listOf(
                0,
                1,
                Int.MAX_VALUE,
                -1
            )

            listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x00, 0x00, 0x00, 0x01),
                byteArrayOf(0x7F.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianInt())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertLittleEndianByteArrayToInt() {

            val expected = listOf(
                0,
                1,
                Int.MAX_VALUE,
                -1
            )

            listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x01, 0x00, 0x00, 0x00),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianInt())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertBigEndianByteArrayToULong() {

            val expected = listOf(
                0u,
                1u,
                Int.MAX_VALUE.toULong(),
                ULong.MAX_VALUE
            )

            listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01),
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x7F.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianULong())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertLittleEndianByteArrayToULong() {

            val expected = listOf(
                0u,
                1u,
                Int.MAX_VALUE.toULong(),
                ULong.MAX_VALUE
            )

            listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianULong())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertBigEndianByteArrayToLong() {

            val expected = listOf(
                0L,
                1L,
                Int.MAX_VALUE.toLong(),
                -1L
            )

            listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01),
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x7F.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianLong())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertLittleEndianByteArrayToLong() {

            val expected = listOf(
                0L,
                1L,
                Int.MAX_VALUE.toLong(),
                -1L
            )

            listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianLong())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertBigEndianByteArrayToFloat() {

            val expected = listOf(
                0f,
                1f,
                Float.MAX_VALUE,
                -1f
            )

            listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x3F, 0x80.toByte(), 0x00, 0x00),
                byteArrayOf(0x7F.toByte(), 0x7F.toByte(), 0xFF.toByte(), 0xFF.toByte()),
                byteArrayOf(0xBF.toByte(), 0x80.toByte(), 0x00, 0x00),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianFloat())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertLittleEndianByteArrayToFloat() {

            val expected = listOf(
                0f,
                1f,
                Float.MAX_VALUE,
                -1f
            )

            listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x00, 0x00, 0x80.toByte(), 0x3F),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x7F.toByte()),
                byteArrayOf(0x00, 0x00, 0x80.toByte(), 0xBF.toByte()),
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianFloat())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual.contentToString(), expected[i])
                    .isEqualTo(expected[i])
            }
        }
    }

    @Nested
    inner class NumbersToByteArray {

        @Test
        internal fun shouldConvertUShortToBigEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00),
                byteArrayOf(0x00, 0x01),
                byteArrayOf(0x7F.toByte(), 0xFF.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte()),
            )

            listOf(
                0u,
                1u,
                Short.MAX_VALUE.toUShort(),
                UShort.MAX_VALUE
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertUShortToLittleEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00),
                byteArrayOf(0x01, 0x00),
                byteArrayOf(0xFF.toByte(), 0x7F.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte()),
            )

            listOf(
                0u,
                1u,
                Short.MAX_VALUE.toUShort(),
                UShort.MAX_VALUE
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertShortToBigEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00),
                byteArrayOf(0x00, 0x01),
                byteArrayOf(0x7F.toByte(), 0xFF.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte()),
            )

            listOf(
                0,
                1,
                Short.MAX_VALUE,
                -1
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertShortToLittleEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00),
                byteArrayOf(0x01, 0x00),
                byteArrayOf(0xFF.toByte(), 0x7F.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte()),
            )

            listOf(
                0,
                1,
                Short.MAX_VALUE,
                -1
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertUIntToBigEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x00, 0x00, 0x00, 0x01),
                byteArrayOf(0x7F.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            )

            listOf(
                0u,
                1u,
                Int.MAX_VALUE.toUInt(),
                UInt.MAX_VALUE
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertUIntToLittleEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x01, 0x00, 0x00, 0x00),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            )

            listOf(
                0u,
                1u,
                Int.MAX_VALUE.toUInt(),
                UInt.MAX_VALUE
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertIntToBigEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x00, 0x00, 0x00, 0x01),
                byteArrayOf(0x7F.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            )

            listOf(
                0,
                1,
                Int.MAX_VALUE,
                -1
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertIntToLittleEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x01, 0x00, 0x00, 0x00),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            )

            listOf(
                0,
                1,
                Int.MAX_VALUE,
                -1
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertULongToBigEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01),
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x7F.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            )

            listOf(
                0u,
                1u,
                Int.MAX_VALUE.toULong(),
                ULong.MAX_VALUE
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertULongToLittleEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            )

            listOf(
                0u,
                1u,
                Int.MAX_VALUE.toULong(),
                ULong.MAX_VALUE
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertLongToBigEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01),
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x7F.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            )

            listOf(
                0L,
                1L,
                Int.MAX_VALUE.toLong(),
                -1L
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertLongToLittleEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            )

            listOf(
                0L,
                1L,
                Int.MAX_VALUE.toLong(),
                -1L
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertFloatToBigEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x3F, 0x80.toByte(), 0x00, 0x00),
                byteArrayOf(0x7F.toByte(), 0x7F.toByte(), 0xFF.toByte(), 0xFF.toByte()),
                byteArrayOf(0xBF.toByte(), 0x80.toByte(), 0x00, 0x00),
            )

            listOf(
                0f,
                1f,
                Float.MAX_VALUE,
                -1f
            ).forEachIndexed { i, actual ->
                assertThat(actual.toBigEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }

        @Test
        internal fun shouldConvertFloatToLittleEndianByteArray() {

            val expected = listOf(
                byteArrayOf(0x00, 0x00, 0x00, 0x00),
                byteArrayOf(0x00, 0x00, 0x80.toByte(), 0x3F),
                byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x7F.toByte()),
                byteArrayOf(0x00, 0x00, 0x80.toByte(), 0xBF.toByte()),
            )

            listOf(
                0f,
                1f,
                Float.MAX_VALUE,
                -1f
            ).forEachIndexed { i, actual ->
                assertThat(actual.toLittleEndianBytes())
                    .withFailMessage("Expected %s <%s> to convert to <%s>", actual.javaClass.name, actual, expected[i].contentToString())
                    .isEqualTo(expected[i])
            }
        }
    }
}
