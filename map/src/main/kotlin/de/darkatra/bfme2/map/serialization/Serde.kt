package de.darkatra.bfme2.map.serialization

internal interface Serde<T> : Serializer<T>, Deserializer<T>
