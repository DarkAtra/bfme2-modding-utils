package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readUShortPrefixedString
import de.darkatra.bfme2.writeUShortPrefixedString
import java.io.InputStream
import java.io.OutputStream

internal class UShortPrefixedStringSerde(
    context: SerializationContext,
    preProcessor: PreProcessor<String>,
    postProcessor: PostProcessor<String>
) : SimpleSerde<String>(
    OutputStream::writeUShortPrefixedString,
    InputStream::readUShortPrefixedString,
    context,
    preProcessor,
    postProcessor
) {

    override fun calculateByteCount(data: String): Long = 2L + data.length
}
