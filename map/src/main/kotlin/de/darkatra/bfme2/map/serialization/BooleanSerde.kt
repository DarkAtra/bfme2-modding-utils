package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.writeBoolean
import java.io.InputStream
import java.io.OutputStream

internal class BooleanSerde(
    context: SerializationContext,
    preProcessor: PreProcessor<Boolean>,
    postProcessor: PostProcessor<Boolean>
) : SimpleSerde<Boolean>(
    OutputStream::writeBoolean,
    InputStream::readBoolean,
    context,
    preProcessor,
    postProcessor
) {

    override fun calculateByteCount(data: Boolean): Long = 1
}
