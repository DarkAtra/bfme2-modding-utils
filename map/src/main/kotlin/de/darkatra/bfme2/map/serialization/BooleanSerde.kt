package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.writeBoolean
import io.goodforgod.graalvm.hint.annotation.ReflectionHint
import java.io.InputStream
import java.io.OutputStream

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
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

    override fun calculateDataSection(data: Boolean): DataSection = DataSectionLeaf.BOOLEAN
}
