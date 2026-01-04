package de.darkatra.bfme2.refpack

import java.io.InputStream

object TestUtils {

    const val UNCOMPRESSED = "/loremipsum.txt"
    const val REFPACK_COMPRESSED = "/loremipsum.refpack"

    fun getInputStream(name: String): InputStream {
        return TestUtils::class.java.getResource(name)!!.openStream().buffered()
    }
}
