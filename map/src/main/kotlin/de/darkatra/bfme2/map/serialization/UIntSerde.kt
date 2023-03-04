package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.writeUInt
import java.io.InputStream
import java.io.OutputStream

internal class UIntSerde(
    context: SerializationContext,
    preProcessor: PreProcessor<UInt>,
    postProcessor: PostProcessor<UInt>
) : SimpleSerde<UInt>(
    OutputStream::writeUInt,
    InputStream::readUInt,
    context,
    preProcessor,
    postProcessor
)
