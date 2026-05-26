package de.darkatra.bfme2.assetdat

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.writeUByte
import java.io.OutputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

internal fun OutputStream.writeUBytePrefixedString(string: String, charset: Charset = StandardCharsets.ISO_8859_1) {
    if (string.length.toUInt() > UByte.MAX_VALUE) {
        throw InvalidDataException("The specified string exceeds the max. allowed length of ${UByte.MAX_VALUE}.")
    }
    this.writeUByte(string.length.toUByte())
    this.write(string.toByteArray(charset))
}
