package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.writeFloat
import java.io.InputStream
import java.io.OutputStream

internal class FloatSerde(
    context: SerializationContext,
    preProcessor: PreProcessor<Float>,
    postProcessor: PostProcessor<Float>
) : SimpleSerde<Float>(
    OutputStream::writeFloat,
    InputStream::readFloat,
    context,
    preProcessor,
    postProcessor
)
