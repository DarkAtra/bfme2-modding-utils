package de.darkatra.bfme2.map

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.asset.AssetList
import de.darkatra.bfme2.map.asset.BuildLists
import de.darkatra.bfme2.map.asset.WorldInfo
import de.darkatra.bfme2.readString
import de.darkatra.bfme2.refpack.RefPackInputStream
import de.darkatra.bfme2.toLittleEndianInt
import org.apache.commons.io.input.CountingInputStream
import java.io.InputStream
import java.io.PushbackInputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.zip.InflaterInputStream

class MapFileReader {

	companion object {
		const val FOUR_CC = "CkMp"

		@JvmStatic
		fun main(args: Array<String>) {
			MapFileReader().read(Path.of("Legendary War.map"))
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
			val assetIndex = reader.readNBytes(4).toLittleEndianInt()
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

		val numberOfAssetStrings = reader.readNBytes(4).toLittleEndianInt()

		val assetNames = mutableMapOf<Int, String>()
		for (i in numberOfAssetStrings downTo 1 step 1) {
			val assetName = reader.readString()
			val assetIndex = reader.readNBytes(4).toLittleEndianInt()
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
			FOUR_CC -> pushbackInputStream.also { it.unread(fourCCBytes) }
			RefPackInputStream.FOUR_CC -> RefPackInputStream(pushbackInputStream.also { it.unread(fourCCBytes) })
			PrefixedInflaterInputStream.FOUR_CC -> InflaterInputStream(pushbackInputStream.also { it.unread(fourCCBytes) })
			else -> throw UnsupportedEncodingException("Encoding is not supported.")
		}
	}

	private fun readAndValidateFourCC(inputStream: InputStream) {
		val fourCC = inputStream.readNBytes(4).toString(StandardCharsets.UTF_8)
		if (fourCC != FOUR_CC) {
			throw InvalidDataException("Invalid four character code. Expected '$FOUR_CC' but found '$fourCC'.")
		}
	}
}

