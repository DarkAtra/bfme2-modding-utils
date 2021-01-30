package de.darkatra.bfme2.refpack

import com.google.common.collect.EvictingQueue
import java.io.FilterInputStream
import java.io.InputStream

class MemorizingInputStream(
	inputStream: InputStream
) : FilterInputStream(inputStream) {

	private val lastReadBytes: EvictingQueue<UByte> = EvictingQueue.create(50)

	override fun read(): Int {
		return super.read().also {
			lastReadBytes.add(it.toUByte())
		}
	}

	override fun read(b: ByteArray): Int {
		return super.read(b).also {
			b.forEach {
				lastReadBytes.add(it.toUByte())
			}
		}
	}

	override fun read(b: ByteArray, off: Int, len: Int): Int {
		return super.read(b, off, len).also {
			b.slice(off until len).forEach {
				lastReadBytes.add(it.toUByte())
			}
		}
	}
}
