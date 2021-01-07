package de.darkatra.bfme2.map

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.SkippingInputStream
import de.darkatra.bfme2.map.reader.AssetListReader
import de.darkatra.bfme2.map.reader.BuildListsReader
import de.darkatra.bfme2.map.reader.GlobalVersionReader
import de.darkatra.bfme2.map.reader.MultiplayerPositionsReader
import de.darkatra.bfme2.map.reader.SidesReader
import de.darkatra.bfme2.map.reader.WorldSettingsReader
import de.darkatra.bfme2.read7BitString
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readShort
import de.darkatra.bfme2.refpack.RefPackInputStream
import org.apache.commons.io.input.CountingInputStream
import java.io.InputStream
import java.io.PushbackInputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.zip.InflaterInputStream

class MapFileReader {

	private val assetListReader = AssetListReader()
	private val buildLists = BuildListsReader()
	private val globalVersionReader = GlobalVersionReader()
	private val multiplayerPositionsReader = MultiplayerPositionsReader()
	private val sidesReader = SidesReader()
	private val worldSettingsReader = WorldSettingsReader()

	companion object {
		const val ASSET_NAME = "Map"
		const val UNCOMPRESSED_FOUR_CC = "CkMp"
		const val REFPACK_FOUR_CC = "EAR\u0000"
		const val ZLIB_FOUR_CC = "ZL5\u0000"

		// TODO: find a better name for this (maybe readList)
		fun readAssets(reader: CountingInputStream, context: MapFileParseContext, callback: (assetName: String) -> Unit) {

			while (reader.byteCount < context.currentEndPosition) {
				val assetIndex = reader.readInt()
				val assetName = context.getAssetName(assetIndex)

				callback(assetName)
			}
		}

		// TODO: find a better name for this (maybe readObject)
		fun readAsset(reader: CountingInputStream, context: MapFileParseContext, assetName: String, callback: (assetVersion: Short) -> Unit) {

			val assetVersion = reader.readShort()

			val dataSize = reader.readInt()
			val startPosition = reader.byteCount
			val endPosition = dataSize + startPosition

			context.push(assetName, endPosition)

			callback(assetVersion)

			context.pop()
		}

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
		context.push(ASSET_NAME, inputFile.length())

		val mapBuilder = MapFile.Builder()

		readAssets(reader, context) { assetName ->
			when (assetName) {
				// TODO: find a better name for ASSET_NAME
				AssetListReader.ASSET_NAME -> assetListReader
				BuildListsReader.ASSET_NAME -> buildLists
				GlobalVersionReader.ASSET_NAME -> globalVersionReader
				MultiplayerPositionsReader.ASSET_NAME -> multiplayerPositionsReader
				SidesReader.ASSET_NAME -> sidesReader
				WorldSettingsReader.ASSET_NAME -> worldSettingsReader
				// TODO: implement the remaining readers... (see OpenFeign)
				else -> throw InvalidDataException("Unknown asset name '$assetName'.")
			}.also {
				it.read(reader, context, mapBuilder)
			}
		}

		return mapBuilder.build()
	}

	// TODO: find a better name for this (maybe readObjectNames)
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

