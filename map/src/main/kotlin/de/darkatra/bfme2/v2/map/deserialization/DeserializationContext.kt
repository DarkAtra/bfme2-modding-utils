package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.v2.map.AssetNameRegistry
import java.util.Stack

internal class DeserializationContext(
    internal val sharedData: MutableMap<String, Any> = mutableMapOf()
) {

    private var assetNameRegistry: AssetNameRegistry? = null

    internal fun setAssetNameRegistry(assetNameRegistry: AssetNameRegistry) {
        this.assetNameRegistry = assetNameRegistry
    }

    internal fun getAssetName(assetIndex: UInt): String {
        return assetNameRegistry!!.assetNames[assetIndex]
            ?: throw IllegalArgumentException("Could not find assetName for assetIndex '$assetIndex'.")
    }

    private val parsingStack: Stack<AssetEntry> = Stack()

    internal val currentEndPosition: Long
        get() = parsingStack.peek().endPosition

    internal fun push(assetName: String, endPosition: Long) {
        parsingStack.push(
            AssetEntry(
                assetName = assetName,
                endPosition = endPosition
            )
        )
    }

    internal fun pop() {
        parsingStack.pop()
    }

    internal data class AssetEntry(
        internal val assetName: String,
        internal val endPosition: Long
    )
}
