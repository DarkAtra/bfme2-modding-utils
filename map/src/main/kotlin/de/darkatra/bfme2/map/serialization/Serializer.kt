package de.darkatra.bfme2.map.serialization

import java.io.OutputStream

internal interface Serializer<T> {

    fun calculateByteCount(data: T): Long

    fun serialize(outputStream: OutputStream, data: T)
}
