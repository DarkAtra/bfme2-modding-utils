package de.darkatra.bfme2.map

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.SkippingInputStream
import de.darkatra.bfme2.map.asset.AssetList
import de.darkatra.bfme2.map.asset.BuildLists
import de.darkatra.bfme2.map.asset.WorldInfo
import de.darkatra.bfme2.read7BitString
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.refpack.RefPackInputStream
import org.apache.commons.io.input.CountingInputStream
import java.io.InputStream
import java.io.PushbackInputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.zip.InflaterInputStream

class MapFileReader {

	companion object {
		const val UNCOMPRESSED_FOUR_CC = "CkMp"
		const val REFPACK_FOUR_CC = "EAR\u0000"
		const val ZLIB_FOUR_CC = "ZL5\u0000"

		@JvmStatic
		fun main(args: Array<String>) {
			MapFileReader().read(Path.of("Legendary War.refpack"))
		}
	}

	fun read(file: Path): MapFile {

		val inputFile = file.toFile()
		val inputStream = CountingInputStream(decodeIfNecessary(inputFile.inputStream()))

		readAndValidateFourCC(inputStream)

		val reader = CountingInputStream(inputStream)
		val assetNames = readAssetNames(reader)

		val context = MapFileParseContext(assetNames)
		context.push("Map", inputFile.length())

		val mapBuilder = MapFile.Builder()

		while (reader.byteCount < context.currentEndPosition) {
			val assetIndex = reader.readInt()
			val assetName = context.getAssetName(assetIndex)
			when (assetName) {
				AssetList.ASSET_NAME -> mapBuilder.assetList(AssetList.read(reader, context))
				BuildLists.ASSET_NAME -> mapBuilder.buildLists(BuildLists.read(reader, context))
				WorldInfo.ASSET_NAME -> mapBuilder.worldInfo(WorldInfo.read(reader, context))
			}
		}

		return mapBuilder.build()
	}

	private fun readAssetNames(reader: CountingInputStream): Map<Int, String> {

		val numberOfAssetStrings = reader.readInt()

		val assetNames = mutableMapOf<Int, String>()
		for (i in numberOfAssetStrings downTo 1 step 1) {
			val assetName = reader.read7BitString()
			val assetIndex = reader.readInt()
			if (assetIndex != i) {
				throw IllegalStateException("Illegal assetIndex for '$assetName'.")
			}
			assetNames[assetIndex] = assetName
		}
		return assetNames
	}

	private fun decodeIfNecessary(inputStream: InputStream): InputStream {

		val pushbackInputStream = PushbackInputStream(inputStream, 4)
		val fourCCBytes = pushbackInputStream.readNBytes(4)

		return when (fourCCBytes.toString(StandardCharsets.UTF_8)) {
			// unread 4 bytes to make it possible to read them again when actually parsing the map data
			UNCOMPRESSED_FOUR_CC -> pushbackInputStream.also { it.unread(fourCCBytes) }
			// skip 4 size bytes, we don't need that information
			REFPACK_FOUR_CC -> RefPackInputStream(SkippingInputStream(pushbackInputStream, 4))
			// skip 4 size bytes, we don't need that information
			ZLIB_FOUR_CC -> InflaterInputStream(SkippingInputStream(pushbackInputStream, 4))
			else -> throw UnsupportedEncodingException("Encoding is not supported.")
		}
	}

	private fun readAndValidateFourCC(inputStream: InputStream) {
		val fourCC = inputStream.readNBytes(4).toString(StandardCharsets.UTF_8)
		if (fourCC != UNCOMPRESSED_FOUR_CC) {
			throw InvalidDataException("Invalid four character code. Expected '$UNCOMPRESSED_FOUR_CC' but found '$fourCC'.")
		}
	}
}

