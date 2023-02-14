package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.SkippingInputStream
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.refpack.RefPackInputStream
import de.darkatra.bfme2.v2.map.AssetNameRegistry
import de.darkatra.bfme2.v2.map.BlendTileDataV18
import de.darkatra.bfme2.v2.map.HeightMapV5
import de.darkatra.bfme2.v2.map.MapFile
import de.darkatra.bfme2.v2.map.WorldInfo
import org.apache.commons.io.IOUtils
import org.apache.commons.io.input.CountingInputStream
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
import kotlin.time.measureTimedValue

class MapFileReader(
    private val debugMode: Boolean = true
) {

    companion object {
        private const val UNCOMPRESSED_FOUR_CC = "CkMp"
        private const val REFPACK_FOUR_CC = "EAR\u0000"
        private const val ZLIB_FOUR_CC = "ZL5\u0000"
    }

    fun read(file: Path): MapFile.Builder {

        if (!file.exists()) {
            throw FileNotFoundException("File '${file.absolutePathString()}' does not exist.")
        }

        return read(file.inputStream())
    }

    fun read(inputStream: InputStream): MapFile.Builder {
        return read(inputStream.buffered())
    }

    // TODO: change return type to MapFile
    @OptIn(ExperimentalTime::class)
    fun read(bufferedInputStream: BufferedInputStream): MapFile.Builder {

        val inputStreamSize = getInputStreamSize(bufferedInputStream)
        val countingInputStream = CountingInputStream(decodeIfNecessary(bufferedInputStream))
        val mapBuilder = MapFile.Builder()

        countingInputStream.use {
            readAndValidateFourCC(countingInputStream)

            val context = measureTimedValue {
                DeserializationContext.Builder().build(inputStreamSize)
            }.also {
                if (debugMode) {
                    println("${DeserializationContext::class.simpleName} creation took ${it.duration}.")
                }
            }.value

            measureTime {
                val assetNameRegistry = ObjectDeserializer(AssetNameRegistry::class, context).deserialize(countingInputStream)
                context.setAssetNameRegistry(assetNameRegistry)
                mapBuilder.assetNameRegistry(assetNameRegistry)
            }.also {
                if (debugMode) {
                    println("Deserialization of '${AssetNameRegistry::class.simpleName}' took $it.")
                }
            }

            readAssets(countingInputStream, context) { assetName ->

                measureTime {
                    when (assetName) {
                        AssetName.HEIGHT_MAP_DATA.assetName -> mapBuilder.heightMapV5(
                            ObjectDeserializer(HeightMapV5::class, context).deserialize(countingInputStream)
                        )

                        AssetName.BLEND_TILE_DATA.assetName -> mapBuilder.blendTileDataV18(
                            ObjectDeserializer(BlendTileDataV18::class, context).deserialize(countingInputStream)
                        )

                        AssetName.WORLD_INFO.assetName -> mapBuilder.worldInfo(
                            ObjectDeserializer(WorldInfo::class, context).deserialize(countingInputStream)
                        )

                        else -> {
                            if (!debugMode) {
                                throw InvalidDataException("Reader for assetName '$assetName' is not implemented.")
                            }
                            countingInputStream.readAllBytes()
                        }
                    }
                }.also {
                    if (debugMode) {
                        println("Deserialization of '$assetName' took $it.")
                    }
                }
            }
        }

        return mapBuilder
    }

    private fun readAssets(inputStream: CountingInputStream, context: DeserializationContext, callback: (assetName: String) -> Unit) {

        while (inputStream.byteCount < context.mapFileSize) {
            val assetIndex = inputStream.readUInt()
            val assetName = context.getAssetName(assetIndex)

            callback(assetName)
        }
    }

    private fun getInputStreamSize(bufferedInputStream: BufferedInputStream): Long {

        if (!bufferedInputStream.markSupported()) {
            throw IllegalArgumentException("Can only parse InputStreams with mark support.")
        }

        bufferedInputStream.mark(Int.MAX_VALUE)
        val inputStreamSize = IOUtils.consume(decodeIfNecessary(bufferedInputStream))
        bufferedInputStream.reset()

        return inputStreamSize
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
            else -> throw UnsupportedEncodingException("Encoding '$fourCCBytes' is not supported.")
        }
    }

    private fun readAndValidateFourCC(inputStream: InputStream) {
        val fourCC = inputStream.readNBytes(4).toString(StandardCharsets.UTF_8)
        if (fourCC != UNCOMPRESSED_FOUR_CC) {
            throw InvalidDataException("Invalid four character code. Expected '$UNCOMPRESSED_FOUR_CC' but found '$fourCC'.")
        }
    }
}

