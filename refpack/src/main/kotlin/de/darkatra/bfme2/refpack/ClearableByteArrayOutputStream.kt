package de.darkatra.bfme2.refpack

import java.io.ByteArrayOutputStream

internal class ClearableByteArrayOutputStream : ByteArrayOutputStream() {

    override fun close() {
        super.reset()
        buf = ByteArray(32)
    }
}
