package de.darkatra.bfme2.map.serialization

import org.apache.commons.io.input.CountingInputStream

internal interface Deserializer<T> {

    fun deserialize(inputStream: CountingInputStream): T
}
