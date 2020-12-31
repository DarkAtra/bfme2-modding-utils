package de.darkatra.bfme2.big

import java.nio.file.Path

class BigArchiveEntry(
	val file: Path,
	val name: String
) {
	val size: Long
		get() = file.toFile().length()
}
