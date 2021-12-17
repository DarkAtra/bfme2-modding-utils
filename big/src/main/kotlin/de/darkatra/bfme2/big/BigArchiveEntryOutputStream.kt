package de.darkatra.bfme2.big

import java.io.ByteArrayOutputStream
import java.io.FilterOutputStream

internal class BigArchiveEntryOutputStream(
	private val bigArchiveEntry: BigArchiveEntry,
	private val outputStream: ByteArrayOutputStream
) : FilterOutputStream(outputStream) {

	override fun flush() {
		bigArchiveEntry.hasPendingChanges = true
		bigArchiveEntry.size = outputStream.size().toUInt()
	}
}
