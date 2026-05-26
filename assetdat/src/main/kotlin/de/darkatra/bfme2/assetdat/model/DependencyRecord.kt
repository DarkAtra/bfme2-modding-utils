package de.darkatra.bfme2.assetdat.model

import de.darkatra.bfme2.assetdat.readUBytePrefixedString
import de.darkatra.bfme2.assetdat.writeUBytePrefixedString
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.writeUShort
import java.io.InputStream
import java.io.OutputStream

internal data class DependencyRecord(
    internal val assetName: String,
    internal val dependencyName: String,
    internal val extraNames: List<String>,
) {

    internal companion object {

        internal fun read(inputStream: InputStream): DependencyRecord {

            val name = inputStream.readUBytePrefixedString()
            val dependencyName = inputStream.readUBytePrefixedString()
            val extraCount = inputStream.readUShort().toUInt()

            val extraNames = mutableListOf<String>()
            for (i in 0u until extraCount) {
                val extraName = inputStream.readUBytePrefixedString()
                extraNames.add(extraName)
            }

            return DependencyRecord(
                assetName = name,
                dependencyName = dependencyName,
                extraNames = extraNames
            )
        }
    }
}

internal fun DependencyRecord.write(outputStream: OutputStream) {

    outputStream.writeUBytePrefixedString(assetName)
    outputStream.writeUBytePrefixedString(dependencyName)

    if (extraNames.size.toUInt() > UShort.MAX_VALUE) {
        throw IllegalArgumentException("Asset '$assetName#$dependencyName' exceeds the max. allowed number of dependencies of ${UShort.MAX_VALUE}.")
    }
    outputStream.writeUShort(extraNames.size.toUShort())
    for (extraName in extraNames) {
        outputStream.writeUBytePrefixedString(extraName)
    }
}
