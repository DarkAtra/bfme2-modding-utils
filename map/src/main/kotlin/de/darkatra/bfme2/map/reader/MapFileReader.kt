package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.SkippingInputStream
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.read7BitString
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.refpack.RefPackInputStream
import org.apache.commons.io.input.CountingInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.PushbackInputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.zip.InflaterInputStream
import kotlin.system.measureTimeMillis

class MapFileReader {

	private val propertyKeyReader = PropertyKeyReader()
	private val propertyReader = PropertyReader(propertyKeyReader)
	private val propertiesReader = PropertiesReader(propertyReader)

	private val assetListReader = AssetListReader()
	private val blendTileDataReader = BlendTileDataReader()
	private val buildListReader = BuildListReader(propertyKeyReader)
	private val buildLists = BuildListsReader(buildListReader)
	private val globalVersionReader = GlobalVersionReader()
	private val heightMapReader = HeightMapReader()
	private val multiplayerPositionsReader = MultiplayerPositionsReader()
	private val playerReader = PlayerReader(buildListReader, propertiesReader)
	private val playerScriptsReader = PlayerScriptsReader()
	private val teamsReader = TeamsReader(propertiesReader)
	private val sidesReader = SidesReader(playerReader, playerScriptsReader, teamsReader)
	private val worldSettingsReader = WorldSettingsReader(propertiesReader)

	companion object {
		const val ASSET_NAME = "Map"
		const val UNCOMPRESSED_FOUR_CC = "CkMp"
		const val REFPACK_FOUR_CC = "EAR\u0000"
		const val ZLIB_FOUR_CC = "ZL5\u0000"

		// TODO: find a better name for this (maybe readList)
		fun readAssets(reader: CountingInputStream, context: MapFileParseContext, callback: (assetName: String) -> Unit) {

			while (reader.byteCount < context.currentEndPosition) {
				val assetIndex = reader.readUInt()
				val assetName = context.getAssetName(assetIndex)

				callback(assetName)
			}
		}

		// TODO: find a better name for this (maybe readObject)
		fun readAsset(reader: CountingInputStream, context: MapFileParseContext, assetName: String, callback: (assetVersion: UShort) -> Unit) {

			val assetVersion = reader.readUShort()

			val dataSize = reader.readUInt()
			val startPosition = reader.byteCount
			val endPosition = dataSize.toLong() + startPosition

			context.push(assetName, endPosition)

			callback(assetVersion)

			context.pop()

			if (reader.byteCount != endPosition) {
				throw InvalidDataException("Error reading '$assetName'. Expected reader to be at position $endPosition, but was at ${reader.byteCount}.")
			}
		}

		@JvmStatic
		fun main(args: Array<String>) {
			MapFileReader().read(Path.of("Legendary War.txt"))
		}
	}

	fun read(file: Path): MapFile {

		val inputFile = file.toFile()

		if (!inputFile.exists()) {
			throw FileNotFoundException("File '${inputFile.absolutePath}' does not exist.")
		}

		val inputStream = CountingInputStream(decodeIfNecessary(inputFile.inputStream().buffered()))

		val mapBuilder = MapFile.Builder()

		inputStream.use {
			readAndValidateFourCC(inputStream)

			val assetNames = readAssetNames(inputStream)

			val context = MapFileParseContext(assetNames)
			// TODO: find a better way to determine the size of the actual data
			context.push(ASSET_NAME, decodeIfNecessary(inputFile.inputStream().buffered()).readBytes().size.toLong())

			readAssets(inputStream, context) { assetName ->
				when (assetName) {
					// TODO: find a better name for ASSET_NAME
					AssetListReader.ASSET_NAME -> assetListReader
					BlendTileDataReader.ASSET_NAME -> blendTileDataReader
					BuildListsReader.ASSET_NAME -> buildLists
					GlobalVersionReader.ASSET_NAME -> globalVersionReader
					HeightMapReader.ASSET_NAME -> heightMapReader
					MultiplayerPositionsReader.ASSET_NAME -> multiplayerPositionsReader
					PlayerScriptsReader.ASSET_NAME -> playerScriptsReader
					SidesReader.ASSET_NAME -> sidesReader
					TeamsReader.ASSET_NAME -> teamsReader
					WorldSettingsReader.ASSET_NAME -> worldSettingsReader
					// TODO: implement the remaining readers... (see OpenFeign)
					else -> throw InvalidDataException("Unknown asset name '$assetName'.")
				}.also {
					val timeElapsedToRead = measureTimeMillis {
						it.read(inputStream, context, mapBuilder)
					}
					println("Reading $assetName took $timeElapsedToRead millis.")
				}
			}
		}

		return mapBuilder.build()
	}

	// TODO: find a better name for this (maybe readObjectNames)
	private fun readAssetNames(reader: CountingInputStream): Map<UInt, String> {

		val numberOfAssetStrings = reader.readUInt()

		val assetNames = mutableMapOf<UInt, String>()
		for (i in numberOfAssetStrings downTo 1u step 1) {
			val assetName = reader.read7BitString()
			val assetIndex = reader.readUInt()
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
