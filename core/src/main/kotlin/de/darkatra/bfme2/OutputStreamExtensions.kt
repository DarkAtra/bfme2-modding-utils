package de.darkatra.bfme2

import java.io.OutputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

fun OutputStream.writeByte(byte: Byte) = this.write(byte.toInt())
fun OutputStream.writeShort(short: Short) = this.write(short.toLittleEndianBytes())
fun OutputStream.writeUShort(uShort: UShort) = this.write(uShort.toLittleEndianBytes())
fun OutputStream.writeInt(int: Int) = this.write(int.toLittleEndianBytes())
fun OutputStream.writeUInt(uInt: UInt) = this.write(uInt.toLittleEndianBytes())
fun OutputStream.writeFloat(float: Float) = this.write(float.toLittleEndianBytes())
fun OutputStream.writeBoolean(boolean: Boolean) = this.writeByte(when (boolean) {
	false -> 0.toByte()
	true -> 1.toByte()
})

fun OutputStream.writeUShortPrefixedString(string: String, charset: Charset = StandardCharsets.US_ASCII) {
	val stringLength = string.length
	if (stringLength.toUInt() > UShort.MAX_VALUE) {
		throw IllegalArgumentException("The specified string exceeds the max. allowed length of ${Short.MAX_VALUE}.")
	}

	this.writeUShort(stringLength.toUShort())
	this.write(string.toByteArray(charset))
}

fun OutputStream.write7BitString(string: String) {

	val bytesToWrite = string.toByteArray(StandardCharsets.UTF_8)

	var uInt = bytesToWrite.size.toUInt()

	while (uInt > Byte.MAX_VALUE.toUInt()) {
		this.writeByte(uInt.or(Byte.MIN_VALUE.toUInt()).toByte())
		uInt = uInt.shr(7)
	}
	this.writeByte(uInt.toByte())

	this.write(bytesToWrite)
}
