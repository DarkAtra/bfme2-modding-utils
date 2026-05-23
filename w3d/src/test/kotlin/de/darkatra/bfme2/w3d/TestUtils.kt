package de.darkatra.bfme2.w3d

import java.io.InputStream

object TestUtils {

    fun getInputStream(name: String): InputStream {
        return TestUtils::class.java.getResource(name)!!.openStream().buffered()
    }
}
