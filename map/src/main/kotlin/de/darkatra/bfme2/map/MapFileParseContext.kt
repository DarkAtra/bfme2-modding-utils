package de.darkatra.bfme2.map

import java.util.Stack

class MapFileParseContext(
	private val assetNames: Map<Int, String>
) {
	private val parsingStack: Stack<AssetEntry> = Stack()

	val currentEndPosition: Long
		get() = parsingStack.peek().endPosition

	var mapHasAssetList: Boolean = false

	fun getAssetName(assetIndex: Int): String = assetNames[assetIndex]
		?: throw IllegalArgumentException("Could not find name for assetIndex '$assetIndex'.")

	fun push(assetType: String, endPosition: Long) {
		parsingStack.push(
			AssetEntry(
				assetType = assetType,
				endPosition = endPosition
			)
		)
	}

	fun pop() {
		parsingStack.pop()
	}
}
