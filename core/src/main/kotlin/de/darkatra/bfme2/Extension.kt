package de.darkatra.bfme2

import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.experimental.and

// Data to Bytes
fun Int.toBigEndianBytes(): ByteArray = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(this).array()
fun Int.toLittleEndianBytes(): ByteArray = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(this).array()
fun UInt.toBigEndianBytes(): ByteArray = toInt().toBigEndianBytes()
fun UInt.toLittleEndianBytes(): ByteArray = toInt().toLittleEndianBytes()

fun Long.toBigEndianBytes(): ByteArray = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(this).array()
fun Long.toLittleEndianBytes(): ByteArray = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(this).array()
fun ULong.toBigEndianBytes(): ByteArray = toLong().toBigEndianBytes()
fun ULong.toLittleEndianBytes(): ByteArray = toLong().toLittleEndianBytes()

// Bytes to Data
fun ByteArray.toBigEndianShort(): Short = ByteBuffer.wrap(this).order(ByteOrder.BIG_ENDIAN).short
fun ByteArray.toLittleEndianShort(): Short = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).short
fun ByteArray.toBigEndianUShort(): UShort = toBigEndianUInt().toUShort()
fun ByteArray.toLittleEndianUShort(): UShort = toLittleEndianUInt().toUShort()

fun ByteArray.toBigEndianInt(): Int = ByteBuffer.wrap(this).order(ByteOrder.BIG_ENDIAN).int
fun ByteArray.toLittleEndianInt(): Int = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).int
fun ByteArray.toBigEndianUInt(): UInt = this.map { it.toUInt() and 0xFFu }.reduce { acc, uInt -> acc shl 8 or uInt }
fun ByteArray.toLittleEndianUInt(): UInt = this.reversedArray().map { it.toUInt() and 0xFFu }.reduce { acc, uInt -> acc shl 8 or uInt }

fun Byte.toBoolean(): Boolean = when (this) {
	0.toByte() -> false
	1.toByte() -> true
	else -> throw ConversionException("Can't convert Byte '$this' to Boolean.")
}

// InputStream
fun InputStream.readByte(): Byte = this.readNBytes(1).first()
fun InputStream.readShort(): Short = this.readNBytes(2).toLittleEndianShort()
fun InputStream.readUShort(): UShort = this.readNBytes(2).toLittleEndianUShort()
fun InputStream.readInt(): Int = this.readNBytes(4).toLittleEndianInt()
fun InputStream.readUInt(): UInt = this.readNBytes(4).toLittleEndianUInt()
fun InputStream.readFloat(): Float = java.lang.Float.intBitsToFloat(readInt())
fun InputStream.readBoolean(): Boolean = this.readByte().toBoolean()

fun InputStream.readShortPrefixedString(charsets: Charset = StandardCharsets.US_ASCII): String {
	val amountOfBytesPerCharacter = when (charsets) {
		StandardCharsets.UTF_8 -> 2
		else -> 1
	}
	val stringLength = this.readNBytes(2).toLittleEndianUShort()
	return this.readNBytes(amountOfBytesPerCharacter * stringLength.toInt()).toString(charsets)
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

@Suppress("UNCHECKED_CAST")
private inline fun <reified T> read2DArray(width: Int, height: Int, readFunction: (x: Int, y: Int) -> T): Array<Array<T>> {
	val result = Array(width) { arrayOfNulls<T>(height) }
	for (y in 0 until width step 1) {
		for (x in 0 until width step 1) {
			result[x][y] = readFunction(x, y)
		}
	}
	return result as Array<Array<T>>
}

inline fun <reified T> InputStream.read2DByteArray(width: Int, height: Int, mappingFunction: (byte: Byte) -> T): Array<Array<T>> {
	return read2DByteArray(width, height).map { innerArray ->
		innerArray.map { mappingFunction(it) }.toTypedArray()
	}.toTypedArray()
}

fun InputStream.read2DShortArray(width: Int, height: Int): Array<Array<Short>> = read2DArray(width, height) { _, _ -> readShort() }
fun InputStream.read2DIntArray(width: Int, height: Int): Array<Array<Int>> = read2DArray(width, height) { _, _ -> readInt() }
fun InputStream.read2DBooleanArray(width: Int, height: Int): Array<Array<Boolean>> = read2DArray(width, height) { _, _ -> readBoolean() }
fun InputStream.read2DByteArray(width: Int, height: Int): Array<Array<Byte>> = read2DArray(width, height) { _, _ -> readByte() }

@Suppress("UNCHECKED_CAST")
fun InputStream.read2DSageBooleanArray(width: Int, height: Int): Array<Array<Boolean>> {
	val result = Array(width) { arrayOfNulls<Boolean>(height) }
	for (y in 0 until width step 1) {
		var temp = 0.toByte()
		for (x in 0 until width step 1) {
			if (x % 8 == 0) {
				temp = readByte()
			}
			result[x][y] = temp and (1 shl x % 8).toByte() != 0.toByte()
		}
	}
	return result as Array<Array<Boolean>>
}

fun Array<Array<Short>>.to2DIntArray(): Array<Array<Int>> {
	return this.map { innerArray ->
		innerArray.map { value -> value.toInt() }.toTypedArray()
	}.toTypedArray()
}
