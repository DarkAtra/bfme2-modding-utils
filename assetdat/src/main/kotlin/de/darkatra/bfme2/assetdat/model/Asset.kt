package de.darkatra.bfme2.assetdat.model

import de.darkatra.bfme2.WindowsFileTimestamp
import de.darkatra.bfme2.assetdat.readUBytePrefixedString
import de.darkatra.bfme2.assetdat.writeUBytePrefixedString
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.toWindowsFileTimestamp
import de.darkatra.bfme2.writeUInt
import de.darkatra.bfme2.writeUShort
import java.io.InputStream
import java.io.OutputStream
import java.time.Instant

data class Asset(
    val name: String,
    val fileTime: Instant,
    val assetEntries: List<AssetEntry>
)

internal fun Asset.write(outputStream: OutputStream) {

    outputStream.writeUBytePrefixedString(name)

    val windowsFileTimestamp = fileTime.toWindowsFileTimestamp()
    outputStream.writeUInt(windowsFileTimestamp.lowDateTime)
    outputStream.writeUInt(windowsFileTimestamp.highDateTime)

    if (assetEntries.size.toUInt() > UShort.MAX_VALUE) {
        throw IllegalArgumentException("Asset '$name' exceeds the max. allowed number of components of ${UShort.MAX_VALUE}.")
    }
    outputStream.writeUShort(assetEntries.size.toUShort())

    for (assetEntry in assetEntries) {
        assetEntry.write(outputStream)
    }
}

internal class IncompleteAsset(
    internal val name: String,
    internal val fileTime: Instant,
    internal val assetEntries: List<IncompleteAssetEntry>
) {

    internal fun toAsset(): Asset {
        return Asset(
            name = name,
            fileTime = fileTime,
            assetEntries = assetEntries.map { it.toAssetEntry() },
        )
    }

    internal companion object {

        internal fun read(inputStream: InputStream): IncompleteAsset {

            val name = inputStream.readUBytePrefixedString()
            val fileTime = WindowsFileTimestamp(
                lowDateTime = inputStream.readUInt(),
                highDateTime = inputStream.readUInt(),
            ).toInstant()

            val assetEntryCount = inputStream.readUShort().toUInt()
            val assetEntries = mutableListOf<IncompleteAssetEntry>()

            for (i in 0u until assetEntryCount) {
                assetEntries.add(IncompleteAssetEntry.read(inputStream))
            }

            return IncompleteAsset(
                name = name,
                fileTime = fileTime,
                assetEntries = assetEntries.sortedBy { it.offset }
            )
        }
    }
}
