package de.darkatra.bfme2.big

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * Represents a single entry in a [BigArchive].
 *
 * @param name The name of the entry.
 */
class BigArchiveEntry(
	val name: String,
	internal val archive: BigArchive,
	internal var offset: UInt = 0u,
	internal var size: UInt = 0u,
	internal var hasPendingChanges: Boolean,
	internal val pendingOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
) {

	/**
	 * Obtains an input stream for this entry.
	 */
	fun inputStream(): InputStream {
		if (hasPendingChanges) {
			return pendingOutputStream.toByteArray().inputStream()
		}

		return BigArchiveEntryInputStream(this)
	}

	/**
	 * Obtains an output stream for this entry.
	 */
	fun outputStream(): OutputStream {
		return BigArchiveEntryOutputStream(this, pendingOutputStream)
	}
}
