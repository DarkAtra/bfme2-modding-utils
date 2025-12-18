package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.writeUShort
import io.goodforgod.graalvm.hint.annotation.ReflectionHint
import java.io.InputStream
import java.io.OutputStream

@ReflectionHint(types = [UShort::class], value = [ReflectionHint.AccessType.ALL_DECLARED])
@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
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

    override fun calculateDataSection(data: UShort): DataSection = DataSectionLeaf.SHORT
}
