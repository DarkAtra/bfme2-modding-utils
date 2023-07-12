package de.darkatra.bfme2.map.serialization

import com.google.common.collect.EvictingQueue
import java.io.FilterInputStream
import java.io.InputStream

internal class RollingWindowInputStream(
    inputStream: InputStream,
    windowSize: Int
) : FilterInputStream(inputStream) {

    private val buffer = EvictingQueue.create<Byte>(windowSize)

    override fun read(): Int {
        val readByte = `in`.read()
        if (readByte != -1) {
            buffer.add(readByte.toByte())
        }
        return readByte
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        val bytesRead = `in`.read(b, off, len)
        if (bytesRead != -1) {
            for (i in off until off + bytesRead) {
                buffer.add(b[i])
            }
        }
        return bytesRead
    }
}
