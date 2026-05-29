package de.darkatra.bfme2.assetdat.model

import de.darkatra.bfme2.assetdat.readUBytePrefixedString
import de.darkatra.bfme2.assetdat.writeUBytePrefixedString
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.writeUInt
import java.io.InputStream
import java.io.OutputStream

data class AssetEntry(
    val name: String,
    val kind: AssetEntryKind,
    val offset: UInt,
    val size: UInt,
    /**
     * Only present for .w3d assets and only for specific kinds:
     * * MESH -> usually texture references
     * * HLOD -> usually subobject / hierarchy / render object references
     * * PART -> likely particle texture/material references
     */
    val dependencyNames: List<String>
)

internal fun AssetEntry.write(outputStream: OutputStream) {

    outputStream.writeUBytePrefixedString(name)
    outputStream.writeUInt(kind.uInt)
    outputStream.writeUInt(offset)
    outputStream.writeUInt(size)
}

internal class IncompleteAssetEntry(
    internal val name: String,
    internal val kind: AssetEntryKind,
    internal val offset: UInt,
    internal val size: UInt,
    internal val dependencyNames: MutableList<String> = mutableListOf()
) {

    internal fun toAssetEntry(): AssetEntry {
        return AssetEntry(
            name = name,
            kind = kind,
            offset = offset,
            size = size,
            dependencyNames = dependencyNames,
        )
    }

    internal companion object {

        internal fun read(inputStream: InputStream): IncompleteAssetEntry {

            val name = inputStream.readUBytePrefixedString()

            return IncompleteAssetEntry(
                name = name,
                kind = AssetEntryKind.ofUInt(inputStream.readUInt()),
                offset = inputStream.readUInt(),
                size = inputStream.readUInt(),
            )
        }
    }
}
