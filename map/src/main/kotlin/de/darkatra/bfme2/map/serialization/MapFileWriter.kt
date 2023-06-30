package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingOutputStream
import de.darkatra.bfme2.PublicApi
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.MapFileCompression
import de.darkatra.bfme2.write7BitIntPrefixedString
import de.darkatra.bfme2.writeUInt
import de.darkatra.bfme2.writeUShort
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
import kotlin.reflect.full.findAnnotation
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class MapFileWriter {

    companion object {

        internal fun <T : Any> writeAsset(outputStream: OutputStream, serializationContext: SerializationContext, data: T, entrySerde: Serde<T>) {

            val asset = data::class.findAnnotation<Asset>()
                ?: throw IllegalStateException("'${data::class.qualifiedName}' must be annotated with '${Asset::class.simpleName}'.")

            val assetIndex = serializationContext.getAssetIndex(asset.name)
            val assetVersion = asset.version

            outputStream.writeUInt(assetIndex)
            outputStream.writeUShort(assetVersion)
            outputStream.writeUInt(entrySerde.calculateDataSection(data).size.toUInt())
        }
    }

    @PublicApi
    fun write(file: Path, mapFile: MapFile) {

        if (file.exists()) {
            throw FileAlreadyExistsException("File '${file.absolutePathString()}' already exist.")
        }

        file.outputStream().use {
            write(it, mapFile)
        }
    }

    fun write(outputStream: OutputStream, mapFile: MapFile, compression: MapFileCompression = MapFileCompression.UNCOMPRESSED) {
        write(outputStream.buffered(), mapFile, compression)
    }

    @OptIn(ExperimentalTime::class)
    fun write(bufferedOutputStream: BufferedOutputStream, mapFile: MapFile, compression: MapFileCompression = MapFileCompression.UNCOMPRESSED) {

        if (compression != MapFileCompression.UNCOMPRESSED) {
            writeFourCC(bufferedOutputStream, compression)
        }

        val countingOutputStream = CountingOutputStream(encodeIfNecessary(bufferedOutputStream, compression))

        writeFourCC(countingOutputStream, MapFileCompression.UNCOMPRESSED)

        val serializationContext = SerializationContext(false)
        val annotationProcessingContext = AnnotationProcessingContext(false)
        val serdeFactory = SerdeFactory(annotationProcessingContext, serializationContext)

        val mapFileSerde: MapFileSerde = serdeFactory.getSerde(MapFile::class) as MapFileSerde
        annotationProcessingContext.invalidate()

        val assetDataSections = mapFileSerde.calculateDataSection(mapFile).flatten()
            .filter { it.isAsset }

        val assetNames = assetDataSections
            .distinctBy { it.assetName }
            .mapIndexed { index, dataSectionHolder -> Pair(index.toUInt() + 1u, dataSectionHolder.assetName!!) }
            .toMap()
        serializationContext.setAssetNames(assetNames)

        measureTime {
            writeAssetNames(assetNames, countingOutputStream)
        }.also { elapsedTime ->
            if (serializationContext.debugMode) {
                println("Writing asset names took $elapsedTime.")
            }
        }

        mapFileSerde.serialize(bufferedOutputStream, mapFile)
        bufferedOutputStream.flush()
    }

    private fun writeAssetNames(assetNames: Map<UInt, String>, outputStream: OutputStream) {

        val numberOfAssetStrings = assetNames.size.toUInt()
        outputStream.writeUInt(numberOfAssetStrings)

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

