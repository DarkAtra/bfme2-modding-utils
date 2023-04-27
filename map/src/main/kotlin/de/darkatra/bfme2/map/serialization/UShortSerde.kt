package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.writeUShort
import java.io.InputStream
import java.io.OutputStream

internal class UShortSerde(
    context: SerializationContext,
    preProcessor: PreProcessor<UShort>,
    postProcessor: PostProcessor<UShort>
) : SimpleSerde<UShort>(
    OutputStream::writeUShort,
    InputStream::readUShort,
    context,
    preProcessor,
    postProcessor
) {

    override fun calculateByteCount(data: UShort): Long = 2
}
