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
	}
}
