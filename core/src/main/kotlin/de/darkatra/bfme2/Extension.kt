package de.darkatra.bfme2

import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets
import kotlin.experimental.and

// Data to Bytes
// TODO: switch to unsigned datatypes
fun Int.toBigEndianBytes(): ByteArray = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(this).array()
fun Int.toLittleEndianBytes(): ByteArray = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(this).array()

// TODO: switch to unsigned datatypes
fun Long.toBigEndianBytes(): ByteArray = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(this).array()
fun Long.toLittleEndianBytes(): ByteArray = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(this).array()

// Bytes to Data
// TODO: switch to unsigned datatypes
fun ByteArray.toBigEndianShort(): Short = ByteBuffer.wrap(this).order(ByteOrder.BIG_ENDIAN).short
fun ByteArray.toLittleEndianShort(): Short = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).short

// TODO: switch to unsigned datatypes
fun ByteArray.toBigEndianInt(): Int = ByteBuffer.wrap(this).order(ByteOrder.BIG_ENDIAN).int
fun ByteArray.toLittleEndianInt(): Int = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).int

// TODO: switch to unsigned datatypes
fun ByteArray.toBigEndianFloat(): Float = ByteBuffer.wrap(this).order(ByteOrder.BIG_ENDIAN).float
fun ByteArray.toLittleEndianFloat(): Float = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).float

fun Byte.toBoolean(): Boolean = when (this) {
	0.toByte() -> false
	1.toByte() -> true
	else -> throw ConversionException("Can't convert Byte '$this' to Boolean.")
}

// InputStream
// TODO: switch to unsigned datatypes
fun InputStream.readByte(): Byte = this.readNBytes(1).first()
fun InputStream.readShort(): Short = this.readNBytes(2).toLittleEndianShort()
fun InputStream.readInt(): Int = this.readNBytes(4).toLittleEndianInt()
fun InputStream.readFloat(): Float = this.readNBytes(4).toLittleEndianFloat()
fun InputStream.readBoolean(): Boolean = this.readByte().toBoolean()

fun InputStream.readShortPrefixedString(): String {
	val stringLength = this.readNBytes(2).toLittleEndianShort()
	return this.readNBytes(stringLength.toInt()).toString(StandardCharsets.US_ASCII)
}

fun InputStream.read7BitString(): String {

	fun determineStringLength(): Int {
		var result = 0

		// first four bytes can be read without worrying about an integer overflow:
		// 1 byte: 0 to 127
		// 2 bytes: 128 to 16,383
		// 3 bytes: 16,384 to 2,097,151
		// 4 bytes: 2,097,152 to 268,435,455
		// 5 bytes: 268,435,456 to 2,147,483,647 (Int.MAX_VALUE) and -2,147,483,648 (Int.MIN_VALUE) to -1
		val maxBytesWithoutOverflow = 4

		for (shift in 0 until maxBytesWithoutOverflow * 7 step 7) {
			val byte = this.readByte()
			result = result or (byte and 0b01111111.toByte()).toInt() shl shift

			// exit early if the byte's int value is not bigger than 127 (meaning the msb is not 1)
			if (byte <= 0b01111111.toByte()) {
				return result
			}
		}

		// read the 5th byte. Since we already read 28 bits, the value of this byte must fit within 4 bits (32 - 28)
		// msb should not be set to 1
		val byte = this.readByte()
		if (byte > 0b1111.toByte()) {
			throw NumberFormatException("Could not read 7bit encoded Int. 5th byte had more than 4 set bits.")
		}
		return result or (byte and 0b01111111.toByte()).toInt() shl (maxBytesWithoutOverflow * 7)
	}

	// read the 7 bit encoded int to determine the length of the string
	val stringLength = determineStringLength()

	if (stringLength < 0) {
		throw IllegalStateException("Invalid String length read from stream: '$stringLength'")
	} else if (stringLength == 0) {
		return ""
	}

	return this.readNBytes(stringLength).toString(StandardCharsets.UTF_8)
}
