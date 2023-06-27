package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.writeInt
import java.io.InputStream
import java.io.OutputStream

internal class IntSerde(
    context: SerializationContext,
    preProcessor: PreProcessor<Int>,
    postProcessor: PostProcessor<Int>
) : SimpleSerde<Int>(
    OutputStream::writeInt,
    InputStream::readInt,
    context,
    preProcessor,
    postProcessor
) {

    override fun calculateDataSection(data: Int): DataSection = DataSectionLeaf.INT
}
