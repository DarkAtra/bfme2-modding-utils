package de.darkatra.bfme2.map

import de.darkatra.bfme2.InvalidDataException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.zip.InflaterInputStream

class PrefixedInflaterInputStream(
	inputStream: InputStream
) : InflaterInputStream(inputStream) {

	companion object {
		const val FOUR_CC = "ZL5\u0000"
	}

	init {
		// read four character code
		val fourCC = `in`.readNBytes(4).toString(StandardCharsets.UTF_8)
		if (fourCC != FOUR_CC) {
			throw InvalidDataException("Invalid four character code. Expected '$FOUR_CC' but found '$fourCC'.")
		}

		// skip uncompressed size bytes
		`in`.skip(4)
	}
}
