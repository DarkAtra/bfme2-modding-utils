package de.darkatra.bfme2.v2.map.deserialization

import org.apache.commons.io.input.CountingInputStream

interface Deserializer<T> {

    fun deserialize(inputStream: CountingInputStream, deserializationContext: DeserializationContext): T
}
