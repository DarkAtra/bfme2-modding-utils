package de.darkatra.bfme2.assetdat.model

import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readNBytes
import de.darkatra.bfme2.readUShort
import java.io.InputStream
import java.nio.charset.StandardCharsets

internal data class DependencyRecord(
    internal val assetName: String,
    internal val dependencyName: String,
    internal val extraNames: List<String>,
) {

    internal companion object {

        internal fun read(inputStream: InputStream): DependencyRecord {

            val name = inputStream.readNBytes(inputStream.readByte().toUInt()).toString(StandardCharsets.ISO_8859_1)
            val dependencyName = inputStream.readNBytes(inputStream.readByte().toUInt()).toString(StandardCharsets.ISO_8859_1)
            val extraCount = inputStream.readUShort().toUInt()

            val extraNames = mutableListOf<String>()
            for (i in 0u until extraCount) {
                val extraName = inputStream.readNBytes(inputStream.readByte().toUInt()).toString(StandardCharsets.ISO_8859_1)
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
