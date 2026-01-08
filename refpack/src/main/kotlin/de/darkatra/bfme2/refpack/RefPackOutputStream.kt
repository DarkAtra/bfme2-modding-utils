package de.darkatra.bfme2.refpack

import de.darkatra.bfme2.refpack.RefPackConstants.MAGIC_HEADER_BYTE
import de.darkatra.bfme2.refpack.RefPackConstants.MAX_REFERENCED_DATA_DISTANCE
import de.darkatra.bfme2.refpack.RefPackConstants.WINDOW_SIZE
import de.darkatra.bfme2.toBigEndianBytes
import de.darkatra.bfme2.writeByte
import de.darkatra.bfme2.writeUByte
import java.io.FilterOutputStream
import java.io.OutputStream
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max
import kotlin.math.min

/**
 * Allows writing RefPack compressed data.
 *
 * Heavily inspired by https://github.com/electronicarts/CnC_Generals_Zero_Hour/blob/main/Generals/Code/Libraries/Source/Compression/EAC/refencode.cpp and https://github.com/ClementJ18/pyBIG/blob/master/pyBIG/refpack.py
 */
class RefPackOutputStream private constructor(
    private val outputStream: OutputStream,
    private val tempBuffer: ClearableByteArrayOutputStream,
) : FilterOutputStream(tempBuffer) {

    private var isFinished = AtomicBoolean(false)

    constructor(
        outputStream: OutputStream,
    ) : this(
        outputStream = outputStream,
        tempBuffer = ClearableByteArrayOutputStream(),
    )

    fun finish() {
        if (isFinished.getAndSet(true)) {
            return
        }

        val data = tempBuffer.toByteArray()

        outputStream.use {
            writeHeader(data)
            writeData(data)
        }
    }

    override fun close() {
        finish()
        super.close()
    }

    private fun writeHeader(data: ByteArray) {

        // write flags
        var flags: UByte = Flags.DEFAULT.uByte

        val isLargeFile = data.size > 0xffffff
        if (isLargeFile) {
            flags = flags or Flags.USE_32BIT_SIZE_HEADER.uByte
        }

        // write flags and magic header byte
        outputStream.writeUByte(flags)
        outputStream.writeUByte(MAGIC_HEADER_BYTE)

        // write uncompressed size
        val sizeBytes = data.size.toBigEndianBytes()

        if (isLargeFile) {
            outputStream.writeByte(sizeBytes[0])
        }
        outputStream.writeByte(sizeBytes[1])
        outputStream.writeByte(sizeBytes[2])
        outputStream.writeByte(sizeBytes[3])
    }

    private fun writeData(data: ByteArray) {

        val hashtable = IntArray(256 * 256) { -1 }
        val link = IntArray(WINDOW_SIZE) { -1 }

        var run = 0
        var rPosition = 0
        var cPosition = 0
        var tLength: Int

        while (cPosition < data.size) {

            var bOffset = 0
            var bLength = 2
            var bCost = 2
            val mLength = when {
                cPosition + 2 >= data.size -> 0
                else -> min(data.size - cPosition, 1028)
            }
            var hash: Int
            var hashOffset: Int
            var mHashOffset: Int

            if (mLength >= 3) {

                hash = hash(data.sliceArray(cPosition until cPosition + 3))
                hashOffset = hashtable[hash]
                mHashOffset = max(cPosition - (MAX_REFERENCED_DATA_DISTANCE - 1), 0)

                while (hashOffset >= mHashOffset) {

                    val tPosition = hashOffset
                    if (cPosition + bLength < data.size
                        && tPosition + bLength < data.size
                        && data[cPosition + bLength] == data[tPosition + bLength]
                    ) {

                        tLength = getMatchLength(data, cPosition, tPosition, mLength)
                        if (tLength > bLength) {
                            val tOffset = (cPosition - 1) - tPosition

                            val tCost = when {
                                tOffset < 1024 && tLength <= 10 -> 2
                                tOffset < 16 * 1024 && tLength <= 67 -> 3
                                else -> 4
                            }

                            if (tLength - tCost + 4 > bLength - bCost + 4) {
                                bLength = tLength
                                bCost = tCost
                                bOffset = tOffset
                                if (bLength >= 1024 + 4) {
                                    break
                                }
                            }
                        }
                    }

                    hashOffset = link[hashOffset and (MAX_REFERENCED_DATA_DISTANCE - 1)]
                }
            }

            if (bCost >= bLength) {
                hash = when {
                    cPosition + 2 < data.size -> hash(data.sliceArray(cPosition until cPosition + 3))
                    else -> 0
                }
                hashOffset = cPosition
                link[hashOffset and (MAX_REFERENCED_DATA_DISTANCE - 1)] = hashtable[hash]
                hashtable[hash] = hashOffset

                run += 1
                cPosition += 1
            } else {
                while (run > 3) {
                    tLength = min(112, run and 3.inv())
                    run -= tLength
                    outputStream.write(0xE0 + (tLength shr 2) - 1)
                    outputStream.write(data.sliceArray(rPosition until rPosition + tLength))
                    rPosition += tLength
                }

                when (bCost) {
                    2 -> {
                        outputStream.write(((bOffset shr 8) shl 5) + ((bLength - 3) shl 2) + run)
                        outputStream.write(bOffset and 0xFF)
                    }

                    3 -> {
                        outputStream.write(0x80 + (bLength - 4))
                        outputStream.write((run shl 6) + (bOffset shr 8))
                        outputStream.write(bOffset and 0xFF)
                    }

                    else -> {
                        outputStream.write(0xC0 + ((bOffset shr 16) shl 4) + (((bLength - 5) shr 8) shl 2) + run)
                        outputStream.write((bOffset shr 8) and 0xFF)
                        outputStream.write(bOffset and 0xFF)
                        outputStream.write((bLength - 5) and 0xFF)
                    }
                }

                if (run > 0) {
                    outputStream.write(data.sliceArray(rPosition until rPosition + run))
                    run = 0
                }

                repeat(bLength) {
                    if (cPosition + 2 < data.size) {
                        hash = hash(data.sliceArray(cPosition until cPosition + 3))
                        hashOffset = cPosition
                        link[hashOffset and (MAX_REFERENCED_DATA_DISTANCE - 1)] = hashtable[hash]
                        hashtable[hash] = hashOffset
                    }
                    cPosition += 1
                }

                rPosition = cPosition
            }
        }

        while (run > 3) {
            tLength = min(112, run and 3.inv())
            run -= tLength
            outputStream.write(0xE0 + (tLength shr 2) - 1)
            outputStream.write(data.sliceArray(rPosition until rPosition + tLength))
            rPosition += tLength
        }

        outputStream.write(0xFC + run)
        if (run > 0) {
            outputStream.write(data.sliceArray(rPosition until rPosition + run))
        }
    }

    private fun hash(data: ByteArray): Int {
        return ((data[0].toInt() shl 4) xor (data[1].toInt() shl 2) xor data[2].toInt()) and 0xFFFF
    }

    private fun getMatchLength(data: ByteArray, cPosition: Int, tPosition: Int, maxMatch: Int): Int {
        var current = 0
        while (current < maxMatch && data[cPosition + current] == data[tPosition + current]) {
            current += 1
        }
        return current
    }
}
