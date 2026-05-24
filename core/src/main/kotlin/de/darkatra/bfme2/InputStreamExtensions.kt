package de.darkatra.bfme2

import java.io.EOFException
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.experimental.or

fun InputStream.readNBytes(length: UInt): ByteArray {
    val lengthAsInt = length.toInt()
    if (lengthAsInt < 0) {
        throw IllegalStateException("Can not read more than ${Int.MAX_VALUE} bytes.")
    }
    return readNBytes(lengthAsInt)
}

fun InputStream.readByte(): Byte {
    val read = this.read()
    if (read == -1) {
        throw EOFException("Unexpected end of input stream")
    }
    return read.toByte()
}

fun InputStream.readUByte(): UByte = this.readByte().toUByte()
fun InputStream.readShort(): Short = this.readNBytes(2).toLittleEndianShort()
fun InputStream.readUShort(): UShort = this.readNBytes(2).toLittleEndianUShort()
fun InputStream.readInt(): Int = this.readNBytes(4).toLittleEndianInt()
fun InputStream.readUInt(): UInt = this.readNBytes(4).toLittleEndianUInt()
fun InputStream.readULong(): ULong = this.readNBytes(8).toLittleEndianULong()
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

fun InputStream.readNullTerminatedString(fixedLength: UInt? = null): String {

    val bytes = mutableListOf<Byte>()
    var nextByte = this.read()
    while (nextByte != 0) {
        if (nextByte == -1) {
            throw InvalidDataException("Unexpected end of stream while reading null terminated string.")
        }
        if (fixedLength != null && bytes.size.toUInt() > fixedLength) {
            throw InvalidDataException("String exceeds the length limit of ${fixedLength}.")
        }
        bytes.add(nextByte.toByte())
        nextByte = this.read()
    }

    // read empty padding
    if (fixedLength != null) {
        val toRead = fixedLength - bytes.size.toUInt() - 1u
        val read = readNBytes(toRead).size.toUInt()
        if (read != toRead) {
            throw InvalidDataException("Some remaining bytes of fixed length string could not be read, missing ${toRead - read}.")
        }
    }

    return bytes.toByteArray().toString(StandardCharsets.US_ASCII)
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
        val byte = this.readUByte()
        result = result or ((byte and 0b01111111.toUByte()).toInt() shl shift)

        // exit early if the most significant bit is not set
        if (byte <= 0b01111111.toUByte()) {
            return result
        }
    }

    // read the 5th byte. Since we already read 28 bits, the value of this byte must fit within the next least significant 4 bits
    val byte = this.readUByte()
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

fun InputStream.exhaust(): Long {
    var total = 0L
    var read: Int
    val buf = ByteArray(DEFAULT_BUFFER_SIZE)
    while (read(buf).also { read = it } != -1) {
        total += read
    }
    return total
}
