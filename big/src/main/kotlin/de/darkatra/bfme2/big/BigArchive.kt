package de.darkatra.bfme2.big

import de.darkatra.bfme2.toBigEndianBytes
import de.darkatra.bfme2.toLittleEndianBytes
import java.io.OutputStream
import java.nio.file.Path

class BigArchive(
	private val version: BigArchiveVersion
) {

	companion object {
		const val HEADER_SIZE = 16
	}

	private val entries = arrayListOf<BigArchiveEntry>()

	fun addFile(file: Path, name: String) {
		entries.add(BigArchiveEntry(file, name))
	}

	fun write(output: OutputStream) {
		entries.sortWith(Comparator.comparing(BigArchiveEntry::name))

		val tableSize = calculateTableSize()
		val contentSize = calculateContentSize()
		val archiveSize: Long = HEADER_SIZE + tableSize + contentSize
		val dataStart: Int = HEADER_SIZE + tableSize

		output.use {
			writeHeader(output, archiveSize, dataStart)
			writeFileTable(output, dataStart)
			writeFileContent(output)

			output.flush()
		}
	}

	private fun writeHeader(output: OutputStream, archiveSize: Long, dataStart: Int) {
		output.write(
			when (version) {
				BigArchiveVersion.BIG_F -> "BIGF".toByteArray()
				BigArchiveVersion.BIG_4 -> "BIG4".toByteArray()
			}
		)

		output.write(archiveSize.toInt().toLittleEndianBytes())
		output.write(entries.size.toBigEndianBytes())
		output.write(dataStart.toBigEndianBytes())
	}

	private fun writeFileTable(output: OutputStream, dataStart: Int) {
		var entryOffset: Long = dataStart.toLong()

		entries.forEach { entry ->
			output.write(entryOffset.toInt().toBigEndianBytes())
			output.write(entry.size.toInt().toBigEndianBytes())
			output.write(entry.name.toByteArray())
			output.write(byteArrayOf(0.toByte()))

			entryOffset += entry.size
		}
	}

	private fun writeFileContent(output: OutputStream) {
		entries.forEach { entry ->
			entry.file.toFile().inputStream().transferTo(output)
		}
	}

	private fun calculateTableSize(): Int {
		// Each entry has 4 bytes for the offset + 4 for size and a null-terminated string
		return entries.fold(0) { acc, entry -> acc + 8 + entry.name.length + 1 }
	}

	private fun calculateContentSize(): Long {
		return entries.fold(0) { acc, entry -> acc + entry.size }
	}
}
