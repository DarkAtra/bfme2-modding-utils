package de.darkatra.bfme2

import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.experimental.or

fun InputStream.readByte(): Byte = this.readNBytes(1).first()
fun InputStream.readShort(): Short = this.readNBytes(2).toLittleEndianShort()
fun InputStream.readUShort(): UShort = this.readNBytes(2).toLittleEndianUShort()
fun InputStream.readInt(): Int = this.readNBytes(4).toLittleEndianInt()
fun InputStream.readUInt(): UInt = this.readNBytes(4).toLittleEndianUInt()
fun InputStream.readFloat(): Float = this.readNBytes(4).toLittleEndianFloat()
fun InputStream.readBoolean(): Boolean = this.readByte().toBoolean()
fun InputStream.readUIntAsBoolean(): Boolean {
    val result = this.readByte().toBoolean()
    val unused = readNBytes(3).reduce { acc, byte -> acc or byte }
    if (unused != 0.toByte()) {
        throw InvalidDataException("Unexpected non empty bytes after boolean found.")
    }
    return result
}

fun InputStream.readUShortPrefixedString(charset: Charset = StandardCharsets.US_ASCII): String {
    val amountOfBytesPerCharacter = when (charset) {
        StandardCharsets.UTF_16LE -> 2
        else -> 1
    }
    val stringLength = this.readUShort()
    return this.readNBytes(amountOfBytesPerCharacter * stringLength.toInt()).toString(charset)
}

fun InputStream.readNullTerminatedString(): String {
    val stringBuilder = StringBuilder()
    var nextByte = this.read()
    while (nextByte != 0) {
        if (nextByte == -1) {
            throw InvalidDataException("Unexpected end of stream while reading null terminated string.")
        }
        stringBuilder.append(nextByte.toChar())
        nextByte = this.read()
    }
    return stringBuilder.toString()
}

fun InputStream.read7BitInt(): Int {

    var result = 0

    // first four bytes can be read without worrying about an integer overflow:
    // 1 byte: 0 to 127
    // 2 bytes: 128 to 16,383
    // 3 bytes: 16,384 to 2,097,151
    // 4 bytes: 2,097,152 to 268,435,455
    // 5 bytes: 268,435,456 to 2,147,483,647 (Int.MAX_VALUE) and -2,147,483,648 (Int.MIN_VALUE) to -1
    val maxBytesWithoutOverflow = 4

    for (shift in 0 until maxBytesWithoutOverflow * 7 step 7) {
        val byte = this.readByte().toUByte()
        result = result or ((byte and 0b01111111.toUByte()).toInt() shl shift)

        // exit early if the most significant bit is not set
        if (byte <= 0b01111111.toUByte()) {
            return result
        }
    }

    // read the 5th byte. Since we already read 28 bits, the value of this byte must fit within the next least significant 4 bits
    val byte = this.readByte().toUByte()
    if (byte > 0b1111.toUByte()) {
        throw NumberFormatException("Could not read 7bit encoded Int. 5th byte had more than 4 least significant bits set.")
    }

    return result or ((byte and 0b01111111.toUByte()).toInt() shl maxBytesWithoutOverflow * 7)
}

fun InputStream.read7BitIntPrefixedString(): String {

    // read the 7 bit encoded int to determine the length of the string
    val stringLength = read7BitInt()

    if (stringLength < 0) {
        throw IllegalStateException("Invalid String length read from stream: '$stringLength'")
    } else if (stringLength == 0) {
        return ""
    }

    return this.readNBytes(stringLength).toString(StandardCharsets.UTF_8)
}
