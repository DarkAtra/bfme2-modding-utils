package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import io.goodforgod.graalvm.hint.annotation.ReflectionHint
import java.io.OutputStream

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
internal abstract class SimpleSerde<T>(
    private val serializationFunction: (outputStream: OutputStream, data: T) -> Unit,
    private val deserializationFunction: (inputStream: CountingInputStream) -> T,
    private val context: SerializationContext,
    private val preProcessor: PreProcessor<T>,
    private val postProcessor: PostProcessor<T>
) : Serde<T> {

    override fun serialize(outputStream: OutputStream, data: T) {
        serializationFunction(
            outputStream,
            preProcessor.preProcess(data, context)
        )
    }

    override fun deserialize(inputStream: CountingInputStream): T {
        return deserializationFunction(inputStream).also {
            postProcessor.postProcess(it, context)
        }
    }
}
