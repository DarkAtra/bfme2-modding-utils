package de.darkatra.bfme2.big

import de.darkatra.bfme2.SkippingInputStream
import java.io.InputStream
import kotlin.io.path.inputStream

internal class BigArchiveEntryInputStream(
	bigArchiveEntry: BigArchiveEntry
) : InputStream() {

	private val inputStream = SkippingInputStream(bigArchiveEntry.archive.path.inputStream(), bigArchiveEntry.offset.toLong())

	override fun read(): Int {
		return inputStream.read()
	}
}
