package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.writeFloat
import io.goodforgod.graalvm.hint.annotation.ReflectionHint
import java.io.InputStream
import java.io.OutputStream

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
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
) {

    override fun calculateDataSection(data: Float): DataSection = DataSectionLeaf.FLOAT
}
