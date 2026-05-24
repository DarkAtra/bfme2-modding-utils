package de.darkatra.bfme2.assetdat.model

import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readNBytes
import de.darkatra.bfme2.readUInt
import java.io.InputStream
import java.nio.charset.StandardCharsets

data class Dependency(
    val name: String,
    val kind: DependencyKind,
    val offset: UInt,
    val size: UInt,
    /**
     * Only present for .w3d assets and only for specific dependency kinds:
     * * MESH -> usually texture references
     * * HLOD -> usually subobject / hierarchy / render object references
     * * PART -> likely particle texture/material references
     */
    val extraDependencyNames: List<String>
)

internal class IncompleteDependency(
    internal val name: String,
    internal val kind: DependencyKind,
    internal val offset: UInt,
    internal val size: UInt,
    internal val extraDependencyNames: MutableList<String> = mutableListOf()
) {

    internal fun toDependency(): Dependency {
        return Dependency(
            name = name,
            kind = kind,
            offset = offset,
            size = size,
            extraDependencyNames = extraDependencyNames,
        )
    }

    internal companion object {

        internal fun read(inputStream: InputStream): IncompleteDependency {

            val name = inputStream.readNBytes(inputStream.readByte().toUInt()).toString(StandardCharsets.ISO_8859_1)

            return IncompleteDependency(
                name = name,
                kind = DependencyKind.ofUInt(inputStream.readUInt()),
                offset = inputStream.readUInt(),
                size = inputStream.readUInt(),
            )
        }
    }
}
