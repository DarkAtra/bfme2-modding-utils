package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import de.darkatra.bfme2.v2.map.ScriptArgument
import de.darkatra.bfme2.v2.map.ScriptArgumentType
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

internal class ScriptArgumentDeserializer(
    private val deserializationContext: DeserializationContext,
    private val postProcessor: PostProcessor<ScriptArgument>
) : Deserializer<ScriptArgument> {

    override fun deserialize(inputStream: CountingInputStream): ScriptArgument {

        val argumentType = ScriptArgumentType.ofId(inputStream.readUInt())

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
