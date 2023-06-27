package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.writeByte
import java.io.InputStream
import java.io.OutputStream

internal class ByteSerde(
    context: SerializationContext,
    preProcessor: PreProcessor<Byte>,
    postProcessor: PostProcessor<Byte>
) : SimpleSerde<Byte>(
    OutputStream::writeByte,
    InputStream::readByte,
    context,
    preProcessor,
    postProcessor
) {

    override fun calculateDataSection(data: Byte): DataSection = DataSectionLeaf.BYTE
}
