package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.model.DataSection
import java.io.OutputStream

internal interface Serializer<T> {

    fun calculateDataSection(data: T): DataSection

    fun serialize(outputStream: OutputStream, data: T)
}
