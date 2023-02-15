package de.darkatra.bfme2.v2.map.deserialization

import java.util.Stack

internal class DeserializationContext(
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

    private val parsingStack: Stack<AssetEntry> = Stack()

    internal val currentEndPosition: Long
        get() = parsingStack.peek().endPosition

    internal fun push(assetEntry: AssetEntry) {
        parsingStack.push(assetEntry)
    }

    internal fun peek(): AssetEntry {
        return parsingStack.peek()
            ?: error("No asset is being processed at this moment. Make sure to use ${DeserializationContext::class.simpleName}#peek only during deserialization.")
    }

    internal fun pop() {
        parsingStack.pop()
    }

    internal data class AssetEntry(
        internal val assetName: String,
        internal val assetVersion: UShort,
        internal val assetSize: Long,
        internal val startPosition: Long
    ) {
        internal val endPosition: Long
            get() = startPosition + assetSize
    }
}
