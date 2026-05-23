package de.darkatra.bfme2.w3d

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.PublicApi
import de.darkatra.bfme2.exhaust
import de.darkatra.bfme2.w3d.model.W3dChunk
import de.darkatra.bfme2.w3d.model.W3dFile
import java.io.BufferedInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.inputStream

class W3dFileReader {

    @PublicApi
    fun read(file: Path): W3dFile {

        if (!file.exists()) {
            throw FileNotFoundException("File '${file.absolutePathString()}' does not exist.")
        }

        return file.inputStream().use(this::read)
    }

    @PublicApi
    fun read(inputStream: InputStream): W3dFile {
        return read(inputStream.buffered())
    }

    @PublicApi
    fun read(bufferedInputStream: BufferedInputStream): W3dFile {

        val inputStreamSize = getInputStreamSize(bufferedInputStream)
        val countingInputStream = CountingInputStream(bufferedInputStream)

        val chunks: List<W3dChunk> = buildList {
            while (countingInputStream.count < inputStreamSize) {
                add(W3dChunk.read(countingInputStream))
            }
        }

        return W3dFile(
            chunks = chunks
        )
    }

    private fun getInputStreamSize(bufferedInputStream: BufferedInputStream): Long {

        if (!bufferedInputStream.markSupported()) {
            throw IllegalArgumentException("Can only parse InputStreams with mark support.")
        }

        bufferedInputStream.mark(Int.MAX_VALUE)
        val inputStreamSize = bufferedInputStream.exhaust()
        bufferedInputStream.reset()

        return inputStreamSize
    }
}
