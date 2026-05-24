package de.darkatra.bfme2.assetdat.model

import de.darkatra.bfme2.WindowsFileTimestamp
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readNBytes
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.time.Instant

data class Asset(
    val name: String,
    val fileTime: Instant,
    val dependencies: List<Dependency>
)

internal class IncompleteAsset(
    internal val name: String,
    internal val fileTime: Instant,
    internal val dependencies: List<IncompleteDependency>
) {

    internal fun toAsset(): Asset {
        return Asset(
            name = name,
            fileTime = fileTime,
            dependencies = dependencies.map { it.toDependency() },
        )
    }

    internal companion object {

        internal fun read(inputStream: InputStream): IncompleteAsset {

            val name = inputStream.readNBytes(inputStream.readByte().toUInt()).toString(StandardCharsets.ISO_8859_1)
            val fileTime = WindowsFileTimestamp(
                lowDateTime = inputStream.readUInt(),
                highDateTime = inputStream.readUInt(),
            ).toInstant()

            val dependencyCount = inputStream.readUShort().toUInt()
            val dependencies = mutableListOf<IncompleteDependency>()

            for (i in 0u until dependencyCount) {
                dependencies.add(IncompleteDependency.read(inputStream))
            }

            return IncompleteAsset(
                name = name,
                fileTime = fileTime,
                dependencies = dependencies
            )
        }
    }
}
