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
    val dependencies: List<Dependency>
)

internal fun Asset.write(outputStream: OutputStream) {

    outputStream.writeUBytePrefixedString(name)

    val windowsFileTimestamp = fileTime.toWindowsFileTimestamp()
    outputStream.writeUInt(windowsFileTimestamp.lowDateTime)
    outputStream.writeUInt(windowsFileTimestamp.highDateTime)

    if (dependencies.size.toUInt() > UShort.MAX_VALUE) {
        throw IllegalArgumentException("Asset '$name' exceeds the max. allowed number of dependencies of ${UShort.MAX_VALUE}.")
    }
    outputStream.writeUShort(dependencies.size.toUShort())

    for (dependency in dependencies) {
        dependency.write(outputStream)
    }
}

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

            val name = inputStream.readUBytePrefixedString()
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
                dependencies = dependencies.sortedBy { it.offset }
            )
        }
    }
}
