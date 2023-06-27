package de.darkatra.bfme2.map.serialization

import java.io.InputStream

object TestUtils {

    const val UNCOMPRESSED_MAP_PATH = "/maps/bfme2-rotwk/Legendary War.txt"
    const val REFPACK_COMPRESSED_MAP_PATH = "/maps/bfme2-rotwk/Legendary War.refpack"
    const val ZLIB_COMPRESSED_MAP_PATH = "/maps/bfme2-rotwk/Legendary War.zlib"

    fun getInputStream(name: String): InputStream {
        return TestUtils::class.java.getResourceAsStream(name)!!
    }
}
