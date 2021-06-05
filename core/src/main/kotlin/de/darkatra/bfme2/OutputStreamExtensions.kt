package de.darkatra.bfme2

import java.io.OutputStream

fun OutputStream.writeUInt(uInt: UInt) = this.write(uInt.toLittleEndianBytes())
