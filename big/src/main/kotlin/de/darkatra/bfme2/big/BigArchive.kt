package de.darkatra.bfme2.big

import de.darkatra.bfme2.PublicApi
import de.darkatra.bfme2.readNullTerminatedString
import de.darkatra.bfme2.toBigEndianBytes
import de.darkatra.bfme2.toBigEndianUInt
import de.darkatra.bfme2.toLittleEndianBytes
import java.io.FileNotFoundException
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

/**
 * Allows reading and writing data from and to big archives.
 *
 * @param version The version of the big archive.
 * @param path The path to the big archive.
 *
 * Heavily inspired by https://github.com/OpenSAGE/OpenSAGE/blob/master/src/OpenSage.FileFormats.Big/BigArchive.cs
 */
class BigArchive(
    @PublicApi
    val version: BigArchiveVersion,
    val path: Path
) {

    companion object {
        const val HEADER_SIZE = 16u // FourCC (4Byte) + UInt (4Byte) + Int (4Byte) + UInt (4Byte)

        fun from(path: Path): BigArchive {
            if (!path.exists()) {
                throw FileNotFoundException("File '${path.absolutePathString()}' does not exist.")
            }

            val fourCCBytes = path.inputStream().use { it.readNBytes(4) }
            if (fourCCBytes.size < 4) {
                throw IllegalStateException("Big archive is too small")
            }

            val version = when (val fourCC = fourCCBytes.toString(StandardCharsets.UTF_8)) {
                "BIGF" -> BigArchiveVersion.BIG_F
                "BIG4" -> BigArchiveVersion.BIG_4
                else -> throw IllegalStateException("Unknown big archive version: '$fourCC'")
            }

            return BigArchive(version, path)
        }
    }

    private val _entries: MutableList<BigArchiveEntry> = arrayListOf()

    @PublicApi
    val entries
        get() = _entries.sortedWith(Comparator.comparing(BigArchiveEntry::name))

    init {
        readFromDisk()
    }

    /**
     * Adds a new entry to the archive.
     *
     * @param name The name of the entry to add.
     */
    fun createEntry(name: String): BigArchiveEntry {
        if (name.isBlank()) {
            throw IllegalArgumentException("Name must not be blank")
        }

        val entry = BigArchiveEntry(
            name = name,
            archive = this,
            hasPendingChanges = true
        )
        _entries.add(entry)
        return entry
    }

    /**
     * Deletes an entry from the archive and writes changes to disk.
     *
     * @param name The name of the entry to delete.
     */
    @PublicApi
    fun deleteEntry(name: String) {
        if (name.isBlank()) {
            throw IllegalArgumentException("Name must not be blank")
        }

        _entries.removeIf { it.name == name }
        writeToDisk()
    }

    /**
     * Reads the archive from disk.
     */
    @PublicApi
    fun readFromDisk() {
        if (!path.exists()) {
            return
        }

        path.inputStream().use {
            it.skip(4) // skip fourCC
            it.readNBytes(4).toBigEndianUInt() // archive size
            val numberOfEntries = it.readNBytes(4).toBigEndianUInt()
            it.readNBytes(4).toBigEndianUInt() // data start

            for (i in 0u until numberOfEntries) {
                val entryOffset = it.readNBytes(4).toBigEndianUInt()
                val entrySize = it.readNBytes(4).toBigEndianUInt()
                val entryName = it.readNullTerminatedString()

                val bigArchiveEntry = BigArchiveEntry(
                    name = entryName,
                    archive = this,
                    offset = entryOffset,
                    size = entrySize,
                    hasPendingChanges = false
                )

                _entries.add(bigArchiveEntry)
            }
        }
    }

    /**
     * Writes changes to disk.
     */
    fun writeToDisk() {
        val output = path.outputStream()
        val tableSize = calculateTableSize()
        val contentSize = calculateContentSize()
        val archiveSize: UInt = HEADER_SIZE + tableSize + contentSize
        val dataStart: UInt = HEADER_SIZE + tableSize

        output.use {
            writeHeader(output, archiveSize, dataStart)
            writeFileTable(output, dataStart)
            writeFileContent(output)

            output.flush()
        }
    }

    private fun writeHeader(output: OutputStream, archiveSize: UInt, dataStart: UInt) {
        output.write(
            when (version) {
                BigArchiveVersion.BIG_F -> "BIGF".toByteArray()
                BigArchiveVersion.BIG_4 -> "BIG4".toByteArray()
            }
        )

        output.write(archiveSize.toLittleEndianBytes())
        output.write(entries.size.toBigEndianBytes())
        output.write(dataStart.toBigEndianBytes())
    }

    private fun writeFileTable(output: OutputStream, dataStart: UInt) {
        var entryOffset: UInt = dataStart

        entries.forEach { entry ->
            output.write(entryOffset.toBigEndianBytes())
            output.write(entry.size.toBigEndianBytes())
            // write the entry name as null terminated string
            output.write(entry.name.toByteArray() + byteArrayOf(0))

            entry.offset = entryOffset
            entryOffset += entry.size
        }
    }

    private fun writeFileContent(output: OutputStream) {
        entries.forEach { entry ->
            output.write(entry.inputStream().use { it.readAllBytes() })
            entry.pendingOutputStream.reset()
            entry.hasPendingChanges = false
        }
    }

    private fun calculateTableSize(): UInt {
        // Each entry has 4 bytes for the offset, 4 bytes for size, n+1 bytes for the null-terminated file name
        return entries.fold(0u) { acc, entry -> acc + 8u + entry.name.length.toUInt() + 1u }
    }

    private fun calculateContentSize(): UInt {
        return entries.fold(0u) { acc, entry -> acc + entry.size }
    }
}
