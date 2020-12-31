package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.toBigEndianInt
import org.apache.commons.io.input.CountingInputStream

class WorldInfo(
	val properties: Map<String, Any>
) : Asset {

	companion object : AssetReader<WorldInfo> {
		const val ASSET_NAME = "WorldInfo"

		override fun read(reader: CountingInputStream, context: MapFileParseContext): WorldInfo {
			return AssetReader.readAsset(reader, context) {
				WorldInfo(
					properties = readProperties(reader, context)
				)
			}
		}

		private fun readProperties(reader: CountingInputStream, context: MapFileParseContext): Map<String, Any> {

			val map: MutableMap<String, Any> = mutableMapOf()
			val numberOfProperties = reader.readNBytes(2).toBigEndianInt()

			for (i in 0 until numberOfProperties step 1) {
				val assetProperty = AssetProperty.read(reader, context)
				map[assetProperty.propertyKey.name] = assetProperty
			}

			return map
		}

	}
}
