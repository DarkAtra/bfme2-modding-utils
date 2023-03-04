package de.darkatra.bfme2.map.serialization

import java.io.OutputStream

internal interface Serializer<T> {

    fun serialize(outputStream: OutputStream, data: T)
}
