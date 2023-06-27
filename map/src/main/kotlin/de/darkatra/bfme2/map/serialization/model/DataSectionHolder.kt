package de.darkatra.bfme2.map.serialization.model

/**
 * Represents a section of data in a map file.
 */
internal interface DataSection {
    val size: Long
}

/**
 * Represents a coherent unit of data in a map file.
 * This could be a string, an integer, a whole array of data or something else entirely.
 * Think of it as a field in a class.
 */
internal data class DataSectionLeaf(
    override val size: Long
) : DataSection {

    companion object {
        val BOOLEAN = DataSectionLeaf(1)
        val BYTE = DataSectionLeaf(1)
        val SHORT = DataSectionLeaf(2)
        val INT = DataSectionLeaf(4)
        val FLOAT = DataSectionLeaf(4)
    }
}

/**
 * Represents multiple sections of data in a map file that somewhat belong together.
 * Think of it as a class, it's basically a container for multiple [DataSection] objects.
 */
internal open class DataSectionHolder(
    internal open val containingData: List<DataSection>,
    internal open val assetName: String? = null,
    internal open val assetVersion: UShort? = null
) : DataSection {

    override val size: Long
        get() = when (isAsset) {
            // each asset has a header of 4 bytes for the assetIndex, 2 bytes for the assetVersion and 4 bytes for the assetSize
            true -> 4 + 2 + 4 + containingData.sumOf(DataSection::size)
            false -> containingData.sumOf(DataSection::size)
        }

    internal open val isAsset: Boolean
        get() = assetName != null && assetVersion != null
}

/**
 * A special [DataSectionHolder] that represents the root of a map file.
 * The only difference to a regular [DataSectionHolder] is that it's an asset but does not require the 10 byte asset header.
 */
internal class MapFileDataSectionHolder(
    containingData: List<DataSection>
) : DataSectionHolder(containingData, "MapFile", 0u) {

    override val size: Long
        get() = containingData.sumOf(DataSection::size)

    override val isAsset: Boolean = true
}
