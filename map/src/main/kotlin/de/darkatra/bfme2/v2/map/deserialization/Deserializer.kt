package de.darkatra.bfme2.v2.map.deserialization

import org.apache.commons.io.input.CountingInputStream

internal interface Deserializer<T> {

    fun deserialize(inputStream: CountingInputStream): T
}
