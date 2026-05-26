package de.darkatra.bfme2.assetdat

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.PublicApi
import de.darkatra.bfme2.assetdat.model.AssetDatFile
import de.darkatra.bfme2.assetdat.model.DependencyRecord
import de.darkatra.bfme2.assetdat.model.IncompleteAsset
import de.darkatra.bfme2.exhaust
import de.darkatra.bfme2.readUInt
import java.io.BufferedInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.inputStream

class AssetDatFileReader {

    /**
     * Reads an asset.dat file from the specified [file] and deserializes it into an [AssetDatFile].
     *
     * @param file the [Path] to the asset.dat file to read
     * @return the deserialized [AssetDatFile]
     */
    @PublicApi
    fun read(file: Path): AssetDatFile {

        if (!file.exists()) {
            throw FileNotFoundException("File '${file.absolutePathString()}' does not exist.")
        }

        return file.inputStream().use(this::read)
    }

    /**
     * Reads an asset.dat file from the specified [inputStream] and deserializes it into an [AssetDatFile].
     * The caller is responsible for closing the provided [inputStream] after use.
     *
     * @param inputStream the [InputStream] to read the asset.dat file from
     * @return the deserialized [AssetDatFile]
     */
    @PublicApi
    fun read(inputStream: InputStream): AssetDatFile {
        return read(inputStream.buffered())
    }

    /**
     * Reads an asset.dat file from the specified [bufferedInputStream] and deserializes it into an [AssetDatFile].
     * The caller is responsible for closing the provided [bufferedInputStream] after use.
     *
     * @param bufferedInputStream the [BufferedInputStream] to read the asset.dat file from
     * @return the deserialized [AssetDatFile]
     */
    @PublicApi
    fun read(bufferedInputStream: BufferedInputStream): AssetDatFile {

        val inputStreamSize = getInputStreamSize(bufferedInputStream)
        val countingInputStream = CountingInputStream(bufferedInputStream)

        readAndValidateFourCC(countingInputStream)
        readAndValidateVersion(countingInputStream)

        val assetCount = countingInputStream.readUInt()
        val dependencyRecordCount = countingInputStream.readUInt()

        val assets = mutableListOf<IncompleteAsset>()
        for (i in 0u until assetCount) {
            assets.add(IncompleteAsset.read(countingInputStream))
        }

        val assetByName = assets.associateBy { it.name }

        for (i in 0u until dependencyRecordCount) {

            val dependencyRecord = DependencyRecord.read(countingInputStream)

            val asset = assetByName[dependencyRecord.assetName]
                ?: continue

            if (!asset.name.endsWith(".w3d")) {
                throw InvalidDataException("Unexpected extra dependency for asset '${asset.name}'.")
            }

            val dependency = asset.dependencies.find { it.name == dependencyRecord.dependencyName }
                ?: continue

            if (!dependency.kind.allowsExtraDependencies) {
                throw InvalidDataException("Unexpected extra dependency for asset '${asset.name}#${dependency.name} (${dependency.kind})'.")
            }

            dependency.extraDependencyNames.addAll(dependencyRecord.extraNames)
        }

        if (countingInputStream.count != inputStreamSize) {
            throw IllegalStateException("Observed trailing bytes while reading asset.dat file. The file might be corrupted.")
        }

        return AssetDatFile(
            assets = assets.map { it.toAsset() },
        )
    }

    private fun getInputStreamSize(bufferedInputStream: BufferedInputStream): Long {

        if (!bufferedInputStream.markSupported()) {
            throw IllegalArgumentException("Can only parse InputStreams with mark support.")
        }

        bufferedInputStream.mark(Int.MAX_VALUE)
        val inputStreamSize = bufferedInputStream.exhaust()
        bufferedInputStream.reset()

        return inputStreamSize
    }

    private fun readAndValidateFourCC(inputStream: InputStream) {
        val fourCC = inputStream.readNBytes(4).toString(StandardCharsets.US_ASCII)
        if (fourCC != AssetDatFile.FOUR_CC) {
            throw InvalidDataException("Invalid four character code. Expected '${AssetDatFile.FOUR_CC}' but found '$fourCC'.")
        }
    }

    private fun readAndValidateVersion(inputStream: InputStream) {
        val version = inputStream.readUInt()
        if (version != AssetDatFile.VERSION) {
            throw InvalidDataException("Invalid version. Expected '0x${AssetDatFile.VERSION.toHexString()}' but found '0x${version.toHexString()}'.")
        }
    }
}
