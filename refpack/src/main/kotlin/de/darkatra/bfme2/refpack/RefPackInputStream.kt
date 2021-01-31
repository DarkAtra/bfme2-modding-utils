package de.darkatra.bfme2.refpack

import de.darkatra.bfme2.InvalidDataException
import java.io.BufferedInputStream
import java.io.InputStream
import kotlin.math.min

/**
 * Allows reading RefPack compressed data.
 *
 * Heavily inspired by https://github.com/OpenSAGE/OpenSAGE/blob/master/src/OpenSage.FileFormats.RefPack/RefPackStream.cs
 */
// TODO: something is broken, compare with OpenSages implementation and fix it
class RefPackInputStream(
	inputStream: InputStream
) : BufferedInputStream(MemorizingInputStream(inputStream, 50)) {

	companion object {
		private const val MAX_REFERENCED_DATA_DISTANCE = 131072
		private const val MAX_BYTES_READ_COUNT = MAX_REFERENCED_DATA_DISTANCE * 300
		private const val WINDOW_SIZE = MAX_REFERENCED_DATA_DISTANCE * 600
	}

	private val decompressedSize: Int
	private var currentPosition: Int = 0
	private var nextPosition: Int = 0
	private var reachedEndOfFile: Boolean = false

	init {

		// read uncompressed size
		val firstByte = `in`.read()
		if (firstByte and 0b00111110 != 0b00010000) {
			throw InvalidDataException("First byte contained invalid data.")
		}

		val largeFilesFlagPresent = firstByte and 0b10000000 != 0
		val compressedSizePresent = firstByte and 0b00000001 != 0

		// check header byte
		val secondByte = `in`.read()
		if (secondByte != 0xFB) {
			throw InvalidDataException("Second byte is not equal to the expected header byte (0xFB).")
		}

		// skip compressedSize bytes, we don't need that information
		if (compressedSizePresent) {
			`in`.skip(if (largeFilesFlagPresent) 4 else 3)
		}

		decompressedSize = readBigEndianSize(largeFilesFlagPresent)
		buf = ByteArray(decompressedSize)
	}

	override fun read(output: ByteArray, offset: Int, count: Int): Int {
		if (count > MAX_BYTES_READ_COUNT) {
			throw InvalidDataException("Attempted to read more than the max allowed amount of bytes ($MAX_BYTES_READ_COUNT).")
		}

		val actualCount = min(count, decompressedSize - currentPosition)

		while (!reachedEndOfFile && currentPosition + actualCount > nextPosition) {
			executeCommand()
		}

		copyTo(currentPosition, output, offset, actualCount)
		currentPosition += actualCount

		return if (actualCount == 0) -1 else actualCount
	}

	private fun executeCommand() {
		val byte1 = `in`.read()
		if (byte1 and 0x80 == 0) {
			execute2ByteCommand(byte1)
		} else if (byte1 and 0x40 == 0) {
			execute3ByteCommand(byte1)
		} else if (byte1 and 0x20 == 0) {
			execute4ByteCommand(byte1)
		} else {
			if (byte1 < 0xFC) {
				execute1ByteCommand(byte1)
			} else {
				execute1ByteCommandAndStop(byte1)
			}
		}
	}

	private fun execute2ByteCommand(byte1: Int) {
		val byte2 = `in`.read()
		val proceedingDataLength = byte1 and 0x03
		copyProceeding(proceedingDataLength)
		val referencedDataLength = ((byte1 and 0x1C) shr 2) + 3
		val referencedDataDistance = ((byte1 and 0x60) shl 3) + byte2 + 1
		copyReferencedData(referencedDataLength, referencedDataDistance)
	}

	private fun execute3ByteCommand(byte1: Int) {
		val byte2 = `in`.read()
		val byte3 = `in`.read()
		val proceedingDataLength: Int = byte2 and 0xC0 shr 6
		copyProceeding(proceedingDataLength)
		val referencedDataLength = (byte1 and 0x3F) + 4
		val referencedDataDistance: Int = ((byte2 and 0x3F) shl 8) + byte3 + 1
		copyReferencedData(referencedDataLength, referencedDataDistance)
	}

	private fun execute4ByteCommand(byte1: Int) {
		val byte2 = `in`.read()
		val byte3 = `in`.read()
		val byte4 = `in`.read()
		val proceedingDataLength = byte1 and 0x03
		copyProceeding(proceedingDataLength)
		val referencedDataLength: Int = ((byte1 and 0x0C) shl 6) + byte4 + 5
		val referencedDataDistance: Int = ((byte1 and 0x10) shl 12) + (byte2 shl 8) + byte3 + 1
		copyReferencedData(referencedDataLength, referencedDataDistance)
	}

	private fun execute1ByteCommand(byte1: Int) {
		val proceedingDataLength = ((byte1 and 0x1F) + 1) shl 2
		copyProceeding(proceedingDataLength)
	}

	private fun execute1ByteCommandAndStop(byte1: Int) {
		val proceedingDataLength = byte1 and 0x03
		copyProceeding(proceedingDataLength)
		reachedEndOfFile = true
	}

	private fun copyProceeding(proceedingDataLength: Int) {
		if (proceedingDataLength > 112) {
			throw InvalidDataException("ProceedingDataLength is greater than 112")
		}

		val readBytes = readFrom(nextPosition, proceedingDataLength)
		if (readBytes != proceedingDataLength) {
			throw InvalidDataException("ReadBytes is not equal to proceedingDataLength")
		}

		nextPosition += proceedingDataLength
	}

	private fun copyReferencedData(referencedDataLength: Int, referencedDataDistance: Int) {
		if (referencedDataDistance > nextPosition) {
			throw InvalidDataException("ReferencedDataDistance > nextPosition")
		}
		if (referencedDataDistance <= 0) {
			throw InvalidDataException("ReferencedDataDistance <= 0")
		}

		// max value for referencedDataDistance is 131072
		if (referencedDataDistance > MAX_REFERENCED_DATA_DISTANCE) {
			throw InvalidDataException("Exceeded max value for referencedDataDistance ($MAX_REFERENCED_DATA_DISTANCE)")
		}

		// copy bytes 1 at a time because it's valid for the referenced data pointer to overflow into the initial value of the output data pointer
		copyFromReferencedData(referencedDataDistance, referencedDataLength)
	}

	private fun readFrom(offset: Int, count: Int): Int {
		return if (offset % WINDOW_SIZE + count > WINDOW_SIZE) {
			val numBytesToWriteAtEnd = WINDOW_SIZE - offset % WINDOW_SIZE
			var bytesRead = `in`.read(buf, offset % WINDOW_SIZE, numBytesToWriteAtEnd)
			bytesRead += `in`.read(buf, 0, count - numBytesToWriteAtEnd)
			bytesRead
		} else {
			`in`.read(buf, offset % WINDOW_SIZE, count)
		}
	}

	private fun copyTo(sourceOffset: Int, destinationBuffer: ByteArray, destinationOffset: Int, count: Int) {
		if (sourceOffset % WINDOW_SIZE + count > WINDOW_SIZE) {
			val numBytesToReadAtEnd = WINDOW_SIZE - sourceOffset % WINDOW_SIZE
			System.arraycopy(buf, sourceOffset % WINDOW_SIZE, destinationBuffer, destinationOffset, numBytesToReadAtEnd)
			System.arraycopy(buf, 0, destinationBuffer, destinationOffset + numBytesToReadAtEnd, count - numBytesToReadAtEnd)
		} else {
			System.arraycopy(buf, sourceOffset % WINDOW_SIZE, destinationBuffer, destinationOffset, count)
		}
	}

	private fun copyFromReferencedData(referencedDataDistance: Int, count: Int) {
		var referencedDataIndex = nextPosition - referencedDataDistance

		// use a simple loop when referencedDataDistance is small and the referenced data overlaps with the current position.
		if (referencedDataDistance < 3 && referencedDataDistance < count) {
			for (i in 0 until count step 1) {
				buf[nextPosition++ % WINDOW_SIZE] = buf[referencedDataIndex++ % WINDOW_SIZE]
			}
			return
		}

		var totalBytesRemaining = count
		while (totalBytesRemaining > 0) {
			val totalBytesToCopy = min(totalBytesRemaining, referencedDataDistance)

			var currentSourceOffset = referencedDataIndex % WINDOW_SIZE
			var currentDestinationOffset = nextPosition % WINDOW_SIZE
			var numBytesRemaining = totalBytesToCopy

			while (numBytesRemaining > 0) {
				val numBytesToCopy = min(min(WINDOW_SIZE - currentSourceOffset, WINDOW_SIZE - currentDestinationOffset), numBytesRemaining)
				System.arraycopy(buf, currentSourceOffset, buf, currentDestinationOffset, numBytesToCopy)
				numBytesRemaining -= numBytesToCopy

				if (numBytesRemaining > 0) {
					currentSourceOffset = (currentSourceOffset + numBytesToCopy) % WINDOW_SIZE
					currentDestinationOffset = (currentDestinationOffset + numBytesToCopy) % WINDOW_SIZE
				}
			}

			nextPosition += totalBytesToCopy
			totalBytesRemaining -= totalBytesToCopy
		}
	}

	private fun readBigEndianSize(largeFilesFlagPresent: Boolean): Int {
		val count = if (largeFilesFlagPresent) 4 else 3
		return `in`.readNBytes(count).map { it.toInt() and 0xFF }.reduce { acc, byte -> acc shl 8 or byte }
	}
}
