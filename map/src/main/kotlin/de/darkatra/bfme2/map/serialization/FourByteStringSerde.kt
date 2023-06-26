package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import java.io.OutputStream
import java.nio.charset.StandardCharsets

internal class FourByteStringSerde(
    private val context: SerializationContext,
    private val preProcessor: PreProcessor<String>,
    private val postProcessor: PostProcessor<String>
) : Serde<String> {

    override fun calculateByteCount(data: String): Long = 4

    override fun serialize(outputStream: OutputStream, data: String) {
        preProcessor.preProcess(data, context).let {
            if (it.length != 4) {
                throw InvalidDataException("Expected '$it' to have a length of exactly four characters.")
            }
            outputStream.write(it.toByteArray(StandardCharsets.UTF_8))
        }
    }

    override fun deserialize(inputStream: CountingInputStream): String {
        return inputStream.readNBytes(4).toString(StandardCharsets.UTF_8).also {
            postProcessor.postProcess(it, context)
        }
    }
}
