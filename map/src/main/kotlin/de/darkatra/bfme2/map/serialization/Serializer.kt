package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.model.DataSection
import java.io.OutputStream

internal interface Serializer<T> {

    // TODO: potentially separate asset name collection and asset size calculations from each other
    // the asset names need to be collected before the actual MapFile is written (probably possible via reflection over MapFile and it's fields)
    // the asset sizes are first needed when the asset is actually serialized
    // not sure if it's worth the effort though
    fun calculateDataSection(data: T): DataSection

    fun serialize(outputStream: OutputStream, data: T)
}
