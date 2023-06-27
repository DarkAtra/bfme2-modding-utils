package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readShort
import de.darkatra.bfme2.writeShort
import java.io.InputStream
import java.io.OutputStream

internal class ShortSerde(
    context: SerializationContext,
    preProcessor: PreProcessor<Short>,
    postProcessor: PostProcessor<Short>
) : SimpleSerde<Short>(
    OutputStream::writeShort,
    InputStream::readShort,
    context,
    preProcessor,
    postProcessor
) {

    override fun collectDataSections(data: Short): DataSection = DataSectionLeaf.SHORT
}
