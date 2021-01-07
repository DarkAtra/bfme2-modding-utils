package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readShort
import org.apache.commons.io.input.CountingInputStream

interface AssetReader<T> {

	companion object {

		inline fun <reified T> readAsset(reader: CountingInputStream, context: MapFileParseContext, callback: (assetVersion: Short) -> T): T {

			val assetVersion = reader.readShort()

			val dataSize = reader.readInt()
			val startPosition = reader.byteCount
			val endPosition = dataSize + startPosition

			context.push(T::class.java.simpleName, endPosition)

			return callback(assetVersion).also {
				context.pop()
			}
		}

		inline fun readAssets(reader: CountingInputStream, context: MapFileParseContext, callback: (assetName: String) -> Unit) {

			while (reader.byteCount < context.currentEndPosition) {
				val assetIndex = reader.readInt()
				val assetName = context.getAssetName(assetIndex)

				callback(assetName)
			}
		}
	}

	fun read(reader: CountingInputStream, context: MapFileParseContext): T
}
