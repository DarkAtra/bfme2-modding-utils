package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.writeUInt
import io.goodforgod.graalvm.hint.annotation.ReflectionHint
import java.io.InputStream
import java.io.OutputStream

@ReflectionHint(types = [UInt::class], value = [ReflectionHint.AccessType.ALL_DECLARED])
@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
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
) {

    override fun calculateDataSection(data: UInt): DataSection = DataSectionLeaf.INT
}
