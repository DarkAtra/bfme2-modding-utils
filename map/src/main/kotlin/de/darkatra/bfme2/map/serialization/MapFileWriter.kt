package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingOutputStream
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.write7BitIntPrefixedString
import de.darkatra.bfme2.writeUInt
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Path
import java.util.zip.DeflaterOutputStream
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.outputStream
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class MapFileWriter {

    @Suppress("unused") // public api
    fun write(file: Path, mapFile: MapFile) {

        if (file.exists()) {
            throw FileAlreadyExistsException("File '${file.absolutePathString()}' already exist.")
        }

        write(file.outputStream(), mapFile)
    }

    fun write(outputStream: OutputStream, mapFile: MapFile, compression: MapFileCompression = MapFileCompression.UNCOMPRESSED) {
        write(outputStream.buffered(), mapFile, compression)
    }

    @OptIn(ExperimentalTime::class)
    fun write(bufferedOutputStream: BufferedOutputStream, mapFile: MapFile, compression: MapFileCompression = MapFileCompression.UNCOMPRESSED) {

        return bufferedOutputStream.use {

            writeFourCC(bufferedOutputStream, compression)

            CountingOutputStream(encodeIfNecessary(bufferedOutputStream, compression)).use { countingOutputStream ->

                writeFourCC(countingOutputStream, MapFileCompression.UNCOMPRESSED)

                val serializationContext = SerializationContext(true)
                val annotationProcessingContext = AnnotationProcessingContext(false)
                val serdeFactory = SerdeFactory(annotationProcessingContext, serializationContext)

                val mapFileSerde = serdeFactory.getSerde(MapFile::class)
                annotationProcessingContext.invalidate()

                measureTime {
                    // FIXME: correctly calculate asset names
                    writeAssetNames(mapOf(), countingOutputStream)
                }.also { elapsedTime ->
                    if (serializationContext.debugMode) {
                        println("Writing asset names took $elapsedTime.")
                    }
                }

                serializationContext.push(
                    SerializationContext.AssetEntry(
                        assetName = "Map",
                        assetVersion = 0u,
                        assetSize = mapFileSerde.calculateByteCount(mapFile),
                        startPosition = 0
                    )
                )


                mapFileSerde.serialize(bufferedOutputStream, mapFile)
                bufferedOutputStream.flush()

                serializationContext.pop()
            }
        }
    }

    private fun writeAssetNames(assetNames: Map<UInt, String>, outputStream: OutputStream) {

        val numberOfAssetStrings = assetNames.size.toUInt()
        for (i in numberOfAssetStrings downTo 1u step 1) {
            outputStream.write7BitIntPrefixedString(assetNames[i]!!)
            outputStream.writeUInt(i)
        }
    }

    private fun encodeIfNecessary(outputStream: OutputStream, compression: MapFileCompression): OutputStream {

        return when (compression) {
            MapFileCompression.UNCOMPRESSED -> outputStream
            MapFileCompression.ZLIB -> DeflaterOutputStream(outputStream)
            else -> throw UnsupportedEncodingException("Encoding '$compression' is not supported.")
        }
    }

    private fun writeFourCC(outputStream: OutputStream, compression: MapFileCompression) {
        when (compression) {
            MapFileCompression.UNCOMPRESSED -> outputStream.write(MapFileCompression.UNCOMPRESSED.fourCC.toByteArray(StandardCharsets.US_ASCII))
            MapFileCompression.REFPACK -> outputStream.write(MapFileCompression.REFPACK.fourCC.toByteArray(StandardCharsets.US_ASCII))
            MapFileCompression.ZLIB -> outputStream.write(MapFileCompression.ZLIB.fourCC.toByteArray(StandardCharsets.US_ASCII))
        }
    }
}

