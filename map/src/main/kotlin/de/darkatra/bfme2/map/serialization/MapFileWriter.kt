package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.PublicApi
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.MapFileCompression
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.write7BitIntPrefixedString
import de.darkatra.bfme2.writeInt
import de.darkatra.bfme2.writeUInt
import de.darkatra.bfme2.writeUShort
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Path
import java.nio.file.StandardOpenOption.CREATE_NEW
import java.nio.file.StandardOpenOption.WRITE
import java.util.zip.DeflaterOutputStream
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.outputStream
import kotlin.reflect.full.findAnnotation
import kotlin.time.measureTime

class MapFileWriter(
    private val debugMode: Boolean = false
) {

    internal companion object {

        internal val sevenBitIntSizeMap = mapOf(
            0..127 to 1,
            128..16383 to 2,
            16384..2097151 to 3,
            2097152..268435455 to 4,
            268435456..Int.MAX_VALUE to 5
        )

        internal fun <T : Any> writeAsset(outputStream: OutputStream, serializationContext: SerializationContext, data: T, entrySerde: Serde<T>) {

            val asset = data::class.findAnnotation<Asset>()
                ?: throw IllegalStateException("'${data::class.qualifiedName}' must be annotated with '${Asset::class.simpleName}'.")

            val assetIndex = serializationContext.getAssetIndex(asset.name)
            val assetVersion = asset.version
            val assetSize = entrySerde.calculateDataSection(data).size.toUInt()

            if (serializationContext.debugMode) {
                println("Writing asset '${asset.name}' with size ${assetSize}.")
            }

            outputStream.writeUInt(assetIndex)
            outputStream.writeUShort(assetVersion)
            outputStream.writeUInt(assetSize)
        }
    }

    @PublicApi
    fun write(file: Path, mapFile: MapFile, compression: MapFileCompression = MapFileCompression.UNCOMPRESSED) {

        if (file.exists()) {
            throw FileAlreadyExistsException("File '${file.absolutePathString()}' already exist.")
        }

        file.outputStream(CREATE_NEW, WRITE).use {
            write(it, mapFile, compression)
        }
    }

    @PublicApi
    fun write(outputStream: OutputStream, mapFile: MapFile, compression: MapFileCompression = MapFileCompression.UNCOMPRESSED) {
        write(outputStream.buffered(), mapFile, compression)
    }

    @PublicApi
    fun write(bufferedOutputStream: BufferedOutputStream, mapFile: MapFile, compression: MapFileCompression = MapFileCompression.UNCOMPRESSED) {

        if (compression == MapFileCompression.REFPACK) {
            throw UnsupportedEncodingException("Encoding '$compression' is not supported.")
        }

        val serializationContext = SerializationContext(debugMode)
        val annotationProcessingContext = AnnotationProcessingContext(debugMode)
        val serdeFactory = SerdeFactory(annotationProcessingContext, serializationContext)

        val mapFileSerde: MapFileSerde = serdeFactory.getSerde(MapFile::class) as MapFileSerde
        annotationProcessingContext.invalidate()

        val mapFileDataSection = mapFileSerde.calculateDataSection(mapFile)
        val assetNames = mapFileDataSection
            .flatten()
            .filter { it.isAsset }
            .distinctBy { it.assetName }
            .mapIndexed { index, dataSectionHolder -> Pair(index.toUInt() + 1u, dataSectionHolder.assetName!!) }
            .toMap()
        serializationContext.setAssetNames(assetNames)

        // mapFileSize = fourCC + assetNames + dataSections
        val mapFileSize = MapFileCompression.UNCOMPRESSED.fourCC.length
            .plus(DataSectionLeaf.INT.size + assetNames.map { assetName ->
                // FIXME: probably breaks if an assetName contains a two byte character
                DataSectionLeaf.INT.size + sevenBitIntSizeMap.entries.find { it.key.contains(assetName.value.length) }!!.value + assetName.value.length
            }.sum())
            .plus(mapFileDataSection.size)
            .toInt()

        if (compression != MapFileCompression.UNCOMPRESSED) {
            writeFourCC(bufferedOutputStream, compression, mapFileSize)
        }

        val encodedOutputStream = encodeIfNecessary(bufferedOutputStream, compression)

        writeFourCC(encodedOutputStream, MapFileCompression.UNCOMPRESSED, mapFileSize)

        measureTime {
            writeAssetNames(assetNames, encodedOutputStream)
        }.also { elapsedTime ->
            if (serializationContext.debugMode) {
                println("Writing asset names took $elapsedTime.")
            }
        }

        mapFileSerde.serialize(encodedOutputStream, mapFile)

        if (encodedOutputStream is DeflaterOutputStream) {
            encodedOutputStream.finish()
        }

        encodedOutputStream.flush()
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
            MapFileCompression.REFPACK -> TODO("Requires RefPackOutputStream which is not implemented yet.")
            MapFileCompression.ZLIB -> DeflaterOutputStream(outputStream)
        }
    }

    private fun writeFourCC(outputStream: OutputStream, compression: MapFileCompression, fileSize: Int) {
        when (compression) {
            MapFileCompression.UNCOMPRESSED -> outputStream.write(MapFileCompression.UNCOMPRESSED.fourCC.toByteArray(StandardCharsets.US_ASCII))
            MapFileCompression.REFPACK -> {
                outputStream.write(MapFileCompression.REFPACK.fourCC.toByteArray(StandardCharsets.US_ASCII))
                outputStream.writeInt(fileSize)
            }

            MapFileCompression.ZLIB -> {
                outputStream.write(MapFileCompression.ZLIB.fourCC.toByteArray(StandardCharsets.US_ASCII))
                outputStream.writeInt(fileSize)
            }
        }
    }
}
