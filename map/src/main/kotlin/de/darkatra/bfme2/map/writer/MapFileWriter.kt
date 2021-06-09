package de.darkatra.bfme2.map.writer

import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.write7BitString
import de.darkatra.bfme2.writeUInt
import de.darkatra.bfme2.writeUShort
import org.apache.commons.io.output.CountingOutputStream
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets

class MapFileWriter {

	// TODO: add remaining writers in correct order
	private val assetWriters = listOf(
		AssetListWriter(),
		GlobalVersionWriter(),
		HeightMapWriter()
	)

	companion object {

		fun writeAsset(writer: CountingOutputStream, version: UShort, assetSize: UInt, writeCallback: () -> Unit) {

			writer.writeUShort(version)
			writer.writeUInt(assetSize)

			val startPosition = writer.byteCount

			writeCallback()

			val endPosition = writer.byteCount

			val writtenBytes = endPosition - startPosition
			if (writtenBytes != assetSize.toLong()) {
				throw IllegalStateException("Expected $assetSize bytes to be written to the OutputStream but got $writtenBytes.")
			}
		}
	}

	fun write(mapFile: MapFile, outputStream: OutputStream) {
		write(mapFile, outputStream.buffered())
	}

	fun write(mapFile: MapFile, outputStream: BufferedOutputStream) {

		outputStream.write("CkMp".toByteArray(StandardCharsets.UTF_8))

		val countingOutputStream = CountingOutputStream(outputStream)

		countingOutputStream.use {

			val context = MapFileComposeContext()

			assetWriters.forEach { assetWriter ->
				assetWriter.composeAssetNames(context, mapFile)
			}

			writeAssetNames(countingOutputStream, context)

			assetWriters.forEach { assetWriter ->
				assetWriter.write(countingOutputStream, context, mapFile)
			}
		}
	}

	private fun writeAssetNames(writer: CountingOutputStream, context: MapFileComposeContext) {

		val assetNames = context.getAssetNames().entries.sortedByDescending { it.value }

		writer.writeUInt(assetNames.size.toUInt())

		assetNames.forEach { assetName ->
			writer.write7BitString(assetName.key)
			writer.writeUInt(assetName.value)
		}
	}
}

