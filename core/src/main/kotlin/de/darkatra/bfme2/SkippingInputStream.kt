package de.darkatra.bfme2

import java.io.FilterInputStream
import java.io.InputStream

class SkippingInputStream(
	input: InputStream,
	bytesToSkip: Long
) : FilterInputStream(input) {

	init {
		skip(bytesToSkip)
	}
}
