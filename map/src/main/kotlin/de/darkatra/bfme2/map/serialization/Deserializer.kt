package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream

internal interface Deserializer<T> {

    fun deserialize(inputStream: CountingInputStream): T
}
