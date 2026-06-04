package de.darkatra.bfme2.assetdat

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.PublicApi
import de.darkatra.bfme2.assetdat.model.AssetDatFile
import de.darkatra.bfme2.assetdat.model.DependencyRecord
import de.darkatra.bfme2.assetdat.model.write
import de.darkatra.bfme2.writeUInt
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Path
import java.nio.file.StandardOpenOption.CREATE_NEW
import java.nio.file.StandardOpenOption.WRITE
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.outputStream

class AssetDatFileWriter {

    /**
     * Writes the given [assetDatFile] to the specified [file].
     *
     * @param file the path to the file where the asset.dat will be written
     * @param assetDatFile the asset.dat file data to write
     */
    @PublicApi
    fun write(file: Path, assetDatFile: AssetDatFile) {

        if (file.exists()) {
            throw FileAlreadyExistsException("File '${file.absolutePathString()}' already exists.")
        }

        file.outputStream(CREATE_NEW, WRITE).use {
            write(it, assetDatFile)
        }
    }

    /**
     * Writes the given [assetDatFile] to the specified [outputStream].
     * The caller is responsible for closing the provided [outputStream] after use.
     *
     * @param outputStream the output stream to write the asset.dat file to
     * @param assetDatFile the asset.dat file data to write
     */
    @PublicApi
    fun write(outputStream: OutputStream, assetDatFile: AssetDatFile) {
        write(outputStream.buffered(), assetDatFile)
    }

    /**
     * Writes the given [assetDatFile] to the specified [bufferedOutputStream].
     * The caller is responsible for closing the provided [bufferedOutputStream] after use.
     *
     * @param bufferedOutputStream the output stream to write the asset.dat file to
     * @param assetDatFile the asset.dat file data to write
     */
    @PublicApi
    fun write(bufferedOutputStream: BufferedOutputStream, assetDatFile: AssetDatFile) {

        writeFourCC(bufferedOutputStream)
        writeVersion(bufferedOutputStream)

        bufferedOutputStream.writeUInt(assetDatFile.assets.size.toUInt())

        val assetEntriesWithDependencies = assetDatFile.assets
            .flatMap { asset -> asset.assetEntries.map { asset to it } }
            .filter { (_, assetEntry) -> assetEntry.dependencyNames.isNotEmpty() }
            .sortedBy { (_, assetEntry) -> assetEntry.name }

        bufferedOutputStream.writeUInt(assetEntriesWithDependencies.size.toUInt())

        for (asset in assetDatFile.assets.sortedBy { it.name }) {
            asset.write(bufferedOutputStream)
        }

        for ((asset, assetEntry) in assetEntriesWithDependencies) {

            if (!asset.name.endsWith(".w3d")) {
                throw InvalidDataException("Unexpected dependency for asset '${asset.name}'.")
            }

            if (!assetEntry.kind.allowsDependencies) {
                throw InvalidDataException("Unexpected dependency for asset '${asset.name}#${assetEntry.name} (${assetEntry.kind})'.")
            }

            val dependencyRecord = DependencyRecord(
                assetName = asset.name,
                dependencyName = assetEntry.name,
                extraNames = assetEntry.dependencyNames
            )

            dependencyRecord.write(bufferedOutputStream)
        }

        bufferedOutputStream.flush()
    }

    private fun writeFourCC(outputStream: OutputStream) {
        outputStream.write(AssetDatFile.FOUR_CC.toByteArray(StandardCharsets.US_ASCII))
    }

    private fun writeVersion(outputStream: OutputStream) {
        outputStream.writeUInt(AssetDatFile.VERSION)
    }
}
