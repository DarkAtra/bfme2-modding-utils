package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.scripting.ScriptArgument
import de.darkatra.bfme2.map.scripting.ScriptArgumentType
import de.darkatra.bfme2.map.serialization.model.DataSection
import de.darkatra.bfme2.map.serialization.model.DataSectionHolder
import de.darkatra.bfme2.map.serialization.model.DataSectionLeaf
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readUShortPrefixedString
import de.darkatra.bfme2.writeFloat
import de.darkatra.bfme2.writeInt
import de.darkatra.bfme2.writeUShortPrefixedString
import java.io.OutputStream

internal class ScriptArgumentSerde(
    serdeFactory: SerdeFactory,
    private val serializationContext: SerializationContext,
    private val preProcessor: PreProcessor<ScriptArgument>,
    private val postProcessor: PostProcessor<ScriptArgument>
) : Serde<ScriptArgument> {

    private val scriptArgumentTypeSerde: Serde<ScriptArgumentType> = serdeFactory.getSerde(ScriptArgumentType::class)

    override fun collectDataSections(data: ScriptArgument): DataSection {
        return DataSectionHolder(
            containingData = listOf(
                scriptArgumentTypeSerde.collectDataSections(data.argumentType),
                when (data.argumentType) {
                    ScriptArgumentType.POSITION_COORDINATE -> DataSectionLeaf(4 * 3)
                    else -> DataSectionLeaf(4L + 4L + 2L + data.stringValue!!.length)
                }
            )
        )
    }

    override fun serialize(outputStream: OutputStream, data: ScriptArgument) {

        preProcessor.preProcess(data, serializationContext).let { scriptArgument ->

            scriptArgumentTypeSerde.serialize(outputStream, scriptArgument.argumentType)

            when (scriptArgument.argumentType) {
                ScriptArgumentType.POSITION_COORDINATE -> {
                    scriptArgument.position!!
                    outputStream.writeFloat(scriptArgument.position.x)
                    outputStream.writeFloat(scriptArgument.position.y)
                    outputStream.writeFloat(scriptArgument.position.z)
                }

                else -> {
                    outputStream.writeInt(scriptArgument.intValue!!)
                    outputStream.writeFloat(scriptArgument.floatValue!!)
                    outputStream.writeUShortPrefixedString(scriptArgument.stringValue!!)
                }
            }
        }
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
