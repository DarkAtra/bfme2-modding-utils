package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.scripting.ScriptArgument
import de.darkatra.bfme2.map.scripting.ScriptArgumentType
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream
import java.io.OutputStream

internal class ScriptArgumentSerde(
    serdeFactory: SerdeFactory,
    private val serializationContext: SerializationContext,
    private val postProcessor: PostProcessor<ScriptArgument>
) : Serde<ScriptArgument> {

    private val scriptArgumentTypeSerde: Serde<ScriptArgumentType> = serdeFactory.getSerde(ScriptArgumentType::class)

    override fun serialize(outputStream: OutputStream, data: ScriptArgument) {
        TODO("Not yet implemented")
    }

    override fun deserialize(inputStream: CountingInputStream): ScriptArgument {

        val argumentType = scriptArgumentTypeSerde.deserialize(inputStream)

        return when (argumentType) {
            ScriptArgumentType.POSITION_COORDINATE -> ScriptArgument(
                argumentType = argumentType,
                position = Vector3(
                    x = inputStream.readFloat(),
                    y = inputStream.readFloat(),
                    z = inputStream.readFloat()
                )
            )

            else -> ScriptArgument(
                argumentType = argumentType,
                intValue = inputStream.readInt(),
                floatValue = inputStream.readFloat(),
                stringValue = inputStream.readUShortPrefixedString()
            )
        }.also {
            postProcessor.postProcess(it, serializationContext)
        }
    }
}
