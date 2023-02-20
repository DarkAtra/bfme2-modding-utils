package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readUShortPrefixedString
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import de.darkatra.bfme2.v2.map.scripting.ScriptArgument
import de.darkatra.bfme2.v2.map.scripting.ScriptArgumentType
import org.apache.commons.io.input.CountingInputStream

internal class ScriptArgumentDeserializer(
    deserializerFactory: DeserializerFactory,
    private val deserializationContext: DeserializationContext,
    private val postProcessor: PostProcessor<ScriptArgument>
) : Deserializer<ScriptArgument> {

    private val scriptArgumentTypeDeserializer: Deserializer<ScriptArgumentType> = deserializerFactory.getDeserializer(ScriptArgumentType::class)

    override fun deserialize(inputStream: CountingInputStream): ScriptArgument {

        val argumentType = scriptArgumentTypeDeserializer.deserialize(inputStream)

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
            postProcessor.postProcess(it, deserializationContext)
        }
    }
}
