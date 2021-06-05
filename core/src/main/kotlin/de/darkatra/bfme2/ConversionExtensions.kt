package de.darkatra.bfme2

import java.nio.ByteBuffer
import java.nio.ByteOrder

// Data to Bytes
fun Short.toBigEndianBytes(): ByteArray = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(this).array()
fun Short.toLittleEndianBytes(): ByteArray = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(this).array()
fun UShort.toBigEndianBytes(): ByteArray = toShort().toBigEndianBytes()
fun UShort.toLittleEndianBytes(): ByteArray = toShort().toLittleEndianBytes()

fun Int.toBigEndianBytes(): ByteArray = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(this).array()
fun Int.toLittleEndianBytes(): ByteArray = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(this).array()
fun UInt.toBigEndianBytes(): ByteArray = toInt().toBigEndianBytes()
fun UInt.toLittleEndianBytes(): ByteArray = toInt().toLittleEndianBytes()

fun Long.toBigEndianBytes(): ByteArray = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(this).array()
fun Long.toLittleEndianBytes(): ByteArray = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(this).array()
fun ULong.toBigEndianBytes(): ByteArray = toLong().toBigEndianBytes()
fun ULong.toLittleEndianBytes(): ByteArray = toLong().toLittleEndianBytes()

fun Boolean.toByte(): Byte = when (this) {
	false -> 0
	true -> 1
}

// Bytes to Data
fun ByteArray.toBigEndianShort(): Short = ByteBuffer.wrap(this).order(ByteOrder.BIG_ENDIAN).short
fun ByteArray.toLittleEndianShort(): Short = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).short
fun ByteArray.toBigEndianUShort(): UShort = toBigEndianUInt().toUShort()
fun ByteArray.toLittleEndianUShort(): UShort = toLittleEndianUInt().toUShort()

fun ByteArray.toBigEndianInt(): Int = ByteBuffer.wrap(this).order(ByteOrder.BIG_ENDIAN).int
fun ByteArray.toLittleEndianInt(): Int = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).int
fun ByteArray.toBigEndianUInt(): UInt = this.map { it.toUInt() and 0xFFu }.reduce { acc, uInt -> acc shl 8 or uInt }
fun ByteArray.toLittleEndianUInt(): UInt = this.reversedArray().map { it.toUInt() and 0xFFu }.reduce { acc, uInt -> acc shl 8 or uInt }

fun ByteArray.toBigEndianLong(): Long = ByteBuffer.wrap(this).order(ByteOrder.BIG_ENDIAN).long
fun ByteArray.toLittleEndianLong(): Long = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).long
fun ByteArray.toBigEndianULong(): ULong = this.map { it.toULong() and 0xFFu }.reduce { acc, uLong -> acc shl 8 or uLong }
fun ByteArray.toLittleEndianULong(): ULong = this.reversedArray().map { it.toULong() and 0xFFu }.reduce { acc, uLong -> acc shl 8 or uLong }

fun ByteArray.toBigEndianFloat(): Float = java.lang.Float.intBitsToFloat(ByteBuffer.wrap(this).order(ByteOrder.BIG_ENDIAN).int)
fun ByteArray.toLittleEndianFloat(): Float = java.lang.Float.intBitsToFloat(ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).int)

fun Byte.toBoolean(): Boolean = when (this) {
	0.toByte() -> false
	1.toByte() -> true
	else -> throw ConversionException("Can't convert Byte '$this' to Boolean.")
}

// TODO: tests
fun Map<UInt, Map<UInt, UShort>>.to2DUIntArrayAsMap(): Map<UInt, Map<UInt, UInt>> {
	return this.mapValues { (_, inner) ->
		inner.mapValues { (_, value) -> value.toUInt() }
	}
}
