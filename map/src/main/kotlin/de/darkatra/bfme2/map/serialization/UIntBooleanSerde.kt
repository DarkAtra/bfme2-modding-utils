package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readUIntAsBoolean
import de.darkatra.bfme2.writeBooleanAsUInt
import java.io.InputStream
import java.io.OutputStream

internal class UIntBooleanSerde(
    context: SerializationContext,
    preProcessor: PreProcessor<Boolean>,
    postProcessor: PostProcessor<Boolean>
) : SimpleSerde<Boolean>(
    OutputStream::writeBooleanAsUInt,
    InputStream::readUIntAsBoolean,
    context,
    preProcessor,
    postProcessor
) {

    override fun calculateByteCount(data: Boolean): Long = 4
}
