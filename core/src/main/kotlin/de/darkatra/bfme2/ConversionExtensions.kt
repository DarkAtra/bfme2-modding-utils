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

fun Float.toBigEndianBytes(): ByteArray = java.lang.Float.floatToIntBits(this).toBigEndianBytes()
fun Float.toLittleEndianBytes(): ByteArray = java.lang.Float.floatToIntBits(this).toLittleEndianBytes()

fun Boolean.toByte(): Byte = when (this) {
    false -> 0
    true -> 1
}

// Bytes to Data
fun ByteArray.toBigEndianShort(): Short = ByteBuffer.wrap(this).order(ByteOrder.BIG_ENDIAN).short
fun ByteArray.toLittleEndianShort(): Short = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).short
fun ByteArray.toBigEndianUShort(): UShort = toBigEndianShort().toUShort()
fun ByteArray.toLittleEndianUShort(): UShort = toLittleEndianShort().toUShort()

fun ByteArray.toBigEndianInt(): Int = ByteBuffer.wrap(this).order(ByteOrder.BIG_ENDIAN).int
fun ByteArray.toLittleEndianInt(): Int = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).int
fun ByteArray.toBigEndianUInt(): UInt = toBigEndianInt().toUInt()
fun ByteArray.toLittleEndianUInt(): UInt = toLittleEndianInt().toUInt()

fun ByteArray.toBigEndianLong(): Long = ByteBuffer.wrap(this).order(ByteOrder.BIG_ENDIAN).long
fun ByteArray.toLittleEndianLong(): Long = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).long
fun ByteArray.toBigEndianULong(): ULong = toBigEndianLong().toULong()
fun ByteArray.toLittleEndianULong(): ULong = toLittleEndianLong().toULong()

fun ByteArray.toBigEndianFloat(): Float = ByteBuffer.wrap(this).order(ByteOrder.BIG_ENDIAN).getFloat()
fun ByteArray.toLittleEndianFloat(): Float = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).getFloat()

fun Byte.toBoolean(): Boolean = when (this) {
    0.toByte() -> false
    1.toByte() -> true
    else -> throw ConversionException("Can't convert Byte '$this' to Boolean.")
}

fun ByteArray.toHexString(): String = joinToString(separator = " ") { eachByte -> "%02x".format(eachByte) }
