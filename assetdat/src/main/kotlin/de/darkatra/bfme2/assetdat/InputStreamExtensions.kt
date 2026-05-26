package de.darkatra.bfme2.assetdat

import de.darkatra.bfme2.readByte
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

internal fun InputStream.readUBytePrefixedString(charset: Charset = StandardCharsets.ISO_8859_1): String {
    val amountOfBytesPerCharacter = when (charset) {
        StandardCharsets.UTF_16LE -> 2
        else -> 1
    }
    val stringLength = this.readByte().toUInt()
    return this.readNBytes(amountOfBytesPerCharacter * stringLength.toInt()).toString(charset)
}
