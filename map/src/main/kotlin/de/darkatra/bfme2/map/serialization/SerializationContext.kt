package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.model.AssetEntry
import de.darkatra.bfme2.map.serialization.model.DataSectionHolder
import java.util.Stack

internal class SerializationContext(
    internal val debugMode: Boolean,
    internal val sharedData: MutableMap<String, Any> = mutableMapOf()
) {

    private var assetNames: Map<UInt, String>? = null

    internal fun setAssetNames(assetNames: Map<UInt, String>) {
        this.assetNames = assetNames
    }

    internal fun getAssetName(assetIndex: UInt): String {
        return assetNames!![assetIndex]
            ?: throw IllegalArgumentException("Could not find assetName for assetIndex '$assetIndex'.")
    }

    internal fun getAssetIndex(assetName: String): UInt {
        return assetNames!!.entries.find { it.value == assetName }?.key
            ?: throw IllegalArgumentException("Could not find assetIndex for assetName '$assetName'.")
    }

    private var assetDataSections: Map<String, DataSectionHolder>? = null

    internal fun setAssetDataSections(assetDataSections: Map<String, DataSectionHolder>) {
        this.assetDataSections = assetDataSections
    }

    internal fun getAssetDataSection(assetName: String): DataSectionHolder {
        return assetDataSections!![assetName]
            ?: throw IllegalArgumentException("Could not find assetIndex for assetName '$assetName'.")
    }

    private val parsingStack: Stack<AssetEntry> = Stack()

    internal val currentEndPosition: Long
        get() = parsingStack.peek().endPosition

    internal fun push(assetEntry: AssetEntry) {
        parsingStack.push(assetEntry)
    }

    internal fun peek(): AssetEntry {
        return parsingStack.peek()
            ?: error("No asset is being processed at this moment. Make sure to use ${SerializationContext::class.simpleName}#peek only during serialization.")
    }

    internal fun pop() {
        parsingStack.pop()
    }
}
