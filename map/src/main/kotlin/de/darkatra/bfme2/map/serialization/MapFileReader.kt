package de.darkatra.bfme2.map.serialization

import com.google.common.io.ByteStreams
import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.SkippingInputStream
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.read7BitIntPrefixedString
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.refpack.RefPackInputStream
import java.io.BufferedInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.PushbackInputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.zip.InflaterInputStream
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class MapFileReader {

    companion object {

        internal fun readAssets(inputStream: CountingInputStream, serializationContext: SerializationContext, callback: (assetName: String) -> Unit) {

            while (inputStream.count < serializationContext.currentEndPosition) {

                val assetIndex = inputStream.readUInt()
                val assetName = serializationContext.getAssetName(assetIndex)

                val currentAsset = AssetEntry(
                    assetName = assetName,
                    assetVersion = inputStream.readUShort(),
                    assetSize = inputStream.readUInt().toLong(),
                    startPosition = inputStream.count
                )

                serializationContext.push(currentAsset)
                callback(assetName)
                serializationContext.pop()

                val currentEndPosition = inputStream.count
                val expectedEndPosition = currentAsset.endPosition
                if (!serializationContext.debugMode && currentEndPosition != expectedEndPosition) {
                    throw InvalidDataException("Error reading '${currentAsset.assetName}'. Expected reader to be at position $expectedEndPosition, but was at $currentEndPosition.")
                }
            }
        }
    }

    @Suppress("unused") // public api
    fun read(file: Path): MapFile {

        if (!file.exists()) {
            throw FileNotFoundException("File '${file.absolutePathString()}' does not exist.")
        }

        return read(file.inputStream())
    }

    fun read(inputStream: InputStream): MapFile {
        return read(inputStream.buffered())
    }

    @OptIn(ExperimentalTime::class)
    fun read(bufferedInputStream: BufferedInputStream): MapFile {

        val inputStreamSize = getInputStreamSize(bufferedInputStream)

        return CountingInputStream(decodeIfNecessary(bufferedInputStream)).use { countingInputStream ->

            readAndValidateFourCC(countingInputStream)

            val serializationContext = SerializationContext(true)
            val annotationProcessingContext = AnnotationProcessingContext(false)
            val serdeFactory = SerdeFactory(annotationProcessingContext, serializationContext)

            measureTime {
                val assetNames = readAssetNames(countingInputStream)
                serializationContext.setAssetNames(assetNames)
            }.also { elapsedTime ->
                if (serializationContext.debugMode) {
                    println("Reading asset names took $elapsedTime.")
                }
            }

            serializationContext.push(
                AssetEntry(
                    assetName = "Map",
                    assetVersion = 0u,
                    assetSize = inputStreamSize,
                    startPosition = 0
                )
            )

            val mapFileSerde = serdeFactory.getSerde(MapFile::class)

            annotationProcessingContext.invalidate()

            val mapFile = mapFileSerde.deserialize(countingInputStream)

            serializationContext.pop()

            mapFile
        }
    }

    private fun getInputStreamSize(bufferedInputStream: BufferedInputStream): Long {

        if (!bufferedInputStream.markSupported()) {
            throw IllegalArgumentException("Can only parse InputStreams with mark support.")
        }

        bufferedInputStream.mark(Int.MAX_VALUE)
        val inputStreamSize = ByteStreams.exhaust(decodeIfNecessary(bufferedInputStream))
        bufferedInputStream.reset()

        return inputStreamSize
    }

    private fun readAssetNames(inputStream: CountingInputStream): Map<UInt, String> {

        val numberOfAssetStrings = inputStream.readUInt()

        val assetNames = mutableMapOf<UInt, String>()
        for (i in numberOfAssetStrings downTo 1u step 1) {
            val assetName = inputStream.read7BitIntPrefixedString()
            val assetIndex = inputStream.readUInt()
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
            MapFileCompression.UNCOMPRESSED.fourCC -> pushbackInputStream.also { it.unread(fourCCBytes) }
            // skip 4 size bytes, we don't need that information
            MapFileCompression.REFPACK.fourCC -> RefPackInputStream(SkippingInputStream(pushbackInputStream, 4))
            // skip 4 size bytes, we don't need that information
            MapFileCompression.ZLIB.fourCC -> InflaterInputStream(SkippingInputStream(pushbackInputStream, 4))
            else -> throw UnsupportedEncodingException("Encoding '$fourCCBytes' is not supported.")
        }
    }

    private fun readAndValidateFourCC(inputStream: InputStream) {
        val fourCC = inputStream.readNBytes(4).toString(StandardCharsets.US_ASCII)
        if (fourCC != MapFileCompression.UNCOMPRESSED.fourCC) {
            throw InvalidDataException("Invalid four character code. Expected '${MapFileCompression.UNCOMPRESSED.fourCC}' but found '$fourCC'.")
        }
    }
}

